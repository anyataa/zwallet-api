package com.zwallet.zwalletapi.Controller;

import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.zwallet.zwalletapi.Config.Encryptor;
import com.zwallet.zwalletapi.Config.JWTUtils;
import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Dto.ChangePasswordDto;
import com.zwallet.zwalletapi.Model.Dto.JWTResponse;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Dto.UserDataFilter;
import com.zwallet.zwalletapi.Model.Dto.UserDetailDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.PhoneNumberEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.PhoneNumberRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Service.AccountService;
import com.zwallet.zwalletapi.Service.UserDetailsImpl;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserDetailController {
    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    AccountService accountService;

    // @PostMapping("/add")
    // public ResponseEntity<?> addUser(@RequestBody UserDetailDto dto) {
    // UserDetailEntity userDetailEntity = new UserDetailEntity(dto.getUsername(),
    // dto.getEmail(), dto.getPassword(),
    // dto.getPin(), dto.getUserFname(), dto.getUserLname(), dto.getUserImage(),
    // dto.getBankNumber(), "USER");

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private Encryptor enc;

    @Autowired
    private PhoneNumberRepository phoneRepository;

    @Autowired
    private AccountRepository accountRepository;

    // ======Add Vendor(Merchant & Bank)======

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDetailDto dto) {
        UserDetailEntity userDetailEntity = new UserDetailEntity();
        AccountDto newAccount = new AccountDto();
        userDetailEntity.setUsername(dto.getUsername());
        userDetailEntity.setEmail(dto.getEmail());
        userDetailEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDetailEntity.setUserRole(dto.getUserRole());
        // userDetailRepository.save(userDetailEntity);
        newAccount.setUser(userDetailEntity);
        accountService.postAccount(newAccount);
        return ResponseEntity.ok().body("Success!");
    }

    // ======Get Users======

    @GetMapping("/all")
    public ResponseEntity<?> getUsers() {
        List<UserDetailEntity> userDetailEntity = userDetailRepository.findAll();

        return ResponseEntity.ok().body(userDetailEntity);
    }

    // ======Sign Up======

    @PostMapping("/signup")
    public ResponseEntity<?> registrasi(@RequestBody UserDetailDto dto) throws ResourceNotFoundException {
        StatusMessageDto<UserDataFilter> response = new StatusMessageDto<>();
        AccountDto newAccount = new AccountDto();
        // checking user exist or not
        UserDetailEntity user = userDetailRepository.findByEmail(dto.getEmail());
        if (user != null) {
            response.setStatus(HttpStatus.EXPECTATION_FAILED.toString());
            response.setMessage("Email already exist!");
            // return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
            return ResponseEntity.ok().body(response);
        }

        // registering account
        try {
            UserDetailEntity userCreated = new UserDetailEntity();
            userCreated.setUsername(dto.getUsername());
            userCreated.setEmail(dto.getEmail());
            userCreated.setPassword(passwordEncoder.encode(dto.getPassword()));
            userCreated.setUserRole("USER");

            // save to repo
            // userService.createUser(userCreated);
            newAccount.setUser(userCreated);
            accountService.postAccount(newAccount);
            AccountEntity foundAccount = accountRepository.findByUserId(userCreated);

            PhoneNumberEntity phone = new PhoneNumberEntity();
            phone.setPhoneNumber(dto.getPhoneNumber());
            phone.setPrimary(true);
            phone.setUser(userCreated);
            phoneRepository.save(phone);
            // User Filter
            UserDataFilter dataFilter = new UserDataFilter(dto.getPhoneNumber(),
                    enc.encryptInt(userCreated.getUserId()), dto.getUsername(), null, dto.getEmail(), null,
                    enc.encryptInt(foundAccount.getAccountId()), newAccount.getBalance());

            response.setStatus(HttpStatus.CREATED.toString());
            response.setMessage("User created!");
            // response.setData(userCreated);
            response.setData(dataFilter);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessage("Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ======Sign In======

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody UserDetailDto dto) {
        StatusMessageDto<JWTResponse> response = new StatusMessageDto<>();

        try {
            // user authentication
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // generate token
            String jwt = jwtUtils.generateJwtToken(authentication);
            // get user principal
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
            // get role
            Set<String> userRoles = userDetailsImpl.getAuthorities().stream().map(userRole -> userRole.getAuthority())
                    .collect(Collectors.toSet());
            Map<String, Object> userData = new HashMap<>();
            UserDetailEntity userDetailEntity = userDetailRepository.findByEmail(userDetailsImpl.getUsername());
            PhoneNumberEntity phoneNumberEntity = phoneRepository.findByUserAndIsPrimary(userDetailEntity, true);
            AccountEntity accountEntity = accountRepository.findByUserId(userDetailEntity);
            // userData.put("user", userDetailEntity);
            // UserDataFilter dataFilter = new UserDataFilter(userDetailEntity.getUserId(),
            // userDetailEntity.getUsername(),
            // userDetailEntity.getUserImage(), phoneNumberEntity.getPhoneNumber());
            userData.put("phoneNumber", phoneNumberEntity.getPhoneNumber());

            userData.put("userId", enc.encryptInt(userDetailEntity.getUserId()));
            // userData.put("userIdClose", (enc.encryptInt(userDetailEntity.getUserId())));
            // userData.put("userIdOpen",
            // enc.decryptString(enc.encryptInt(userDetailEntity.getUserId())));
            // userData.put("userId", encryptInt(userDetailEntity.getUserId()));
            // userData.put("openUserId",
            // decryptString(encryptInt(userDetailEntity.getUserId())));
            userData.put("userName", userDetailEntity.getUsername());
            userData.put("userImage", userDetailEntity.getUserImage());
            userData.put("userEmail", userDetailEntity.getEmail());
            userData.put("userPin", userDetailEntity.getPin());
            // userData.put("accountId", accountEntity.getAccountId());
            userData.put("accountId", enc.encryptInt(accountEntity.getAccountId()));
            userData.put("accountBalance", accountEntity.getBalance());
            // userData.put("account", accountEntity);

            response.setStatus(HttpStatus.OK.toString());
            response.setMessage("Login success!");
            response.setData(new JWTResponse(jwt, userDetailsImpl.getUsername(), userRoles, userData));
            // response.setData(new JWTResponse(jwt, userDetailsImpl.getUsername(),
            // userRoles, dataFilter));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessage("Error: " + e.getMessage());

            return ResponseEntity.ok().body("Invalid Data");
        }
    }

    // ======Soft Delete======

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setDeleted(true);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body(userEntity);
    }

    // ======Update User Status When Sign In======

    @PutMapping("/signin-status/{id}")
    public ResponseEntity<?> updateSignStatus(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setActive(true);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Login");
    }

    // ======Update User Status When Sign Out======

    @PutMapping("/signout-status/{id}")
    public ResponseEntity<?> updateSignOutStatus(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setActive(false);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Logout");
    }

    // ON GOING
    // ======Change Password - Profile======

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();

        // userEntity.setPassword(passwordEncoder.encode(dto.getCurrentPass()));
        // userEntity.setPassword(passwordEncoder.encode(dto.getNewPass()));
        // userEntity.setPassword(passwordEncoder.encode(dto.getConfirmPass()));

        if (passwordEncoder.matches(dto.getCurrentPass(), userEntity.getPassword())) {
            try {
                userEntity.setPassword(passwordEncoder.encode(dto.getNewPass()));
                userDetailRepository.save(userEntity);

                Map<String, Object> userData = new HashMap<>();
                PhoneNumberEntity phoneNumberEntity = phoneRepository.findByUserAndIsPrimary(userEntity, true);
                AccountEntity accountEntity = accountRepository.findByUserId(userEntity);
                userData.put("phoneNumber", phoneNumberEntity.getPhoneNumber());
                userData.put("userId", userEntity.getUserId());
                userData.put("userName", userEntity.getUsername());
                userData.put("userImage", userEntity.getUserImage());
                userData.put("userEmail", userEntity.getEmail());
                userData.put("userPin", userEntity.getPin());
                userData.put("accountId", accountEntity.getAccountId());
                userData.put("accountBalance", accountEntity.getBalance());

                return ResponseEntity.ok().body(userData);
                // return ResponseEntity.ok().body("Password Changed Successfully");
            } catch (Exception e) {
                return ResponseEntity.ok().body("Failed");
            }
        } else {
            return ResponseEntity.ok().body("Error");
        }

    }

    // ======Create & New PIN======

    @PutMapping("/update-pin/{id}")
    public ResponseEntity<?> updatePin(@RequestBody UserDetailDto dto, @PathVariable String id) {
        Integer openId = enc.decryptString(id);
        UserDetailEntity userEntity = userDetailRepository.findById(openId).get();
        userEntity.setPin(dto.getPin());
        userDetailRepository.save(userEntity);

        Map<String, Object> userData = new HashMap<>();
        UserDetailEntity userDetailEntity = userDetailRepository.findById(openId).get();
        PhoneNumberEntity phoneNumberEntity = phoneRepository.findByUserAndIsPrimary(userDetailEntity, true);
        AccountEntity accountEntity = accountRepository.findByUserId(userDetailEntity);
        userData.put("phoneNumber", phoneNumberEntity.getPhoneNumber());
        userData.put("userId", userDetailEntity.getUserId());
        userData.put("userName", userDetailEntity.getUsername());
        userData.put("userImage", userDetailEntity.getUserImage());
        userData.put("userEmail", userDetailEntity.getEmail());
        userData.put("userPin", userDetailEntity.getPin());
        userData.put("accountId", accountEntity.getAccountId());
        userData.put("accountBalance", accountEntity.getBalance());

        return ResponseEntity.ok().body(userData);
    }

    // ======Reset Password : Login page======

    @PutMapping("/reset-password/{id}")
    public ResponseEntity<?> resetPassword(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDetailRepository.save(userEntity);

        Map<String, Object> userData = new HashMap<>();
        UserDetailEntity userDetailEntity = userDetailRepository.findById(id).get();
        userData.put("userId", userDetailEntity.getUserId());
        userData.put("userEmail", userDetailEntity.getEmail());

        return ResponseEntity.ok().body("Password Changed");
    }

    // ======Personal Information======

    @PutMapping("/personalinfo/{id}")
    public ResponseEntity<?> addPersonalInformation(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setUserFname(dto.getUserFname());
        userEntity.setUserLname(dto.getUserLname());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Profile Has Been Saved Successfully!");
    }

    // ======Bank Number======

    @PutMapping("/banknumber/{id}")
    public ResponseEntity<?> addBankNumber(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setBankNumber(dto.getBankNumber());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Bank Account Number Has Been Registered Successfully! "
                + "Your Bank Account Number : " + dto.getBankNumber());
    }

    // @GetMapping("/pin")
    // // Low : Secure
    // // buat 1 dtoChangePin , yang ngirim 2 (pinSekarang, id)
    // public ResponseEntity<?> checkPin(@RequestBody String pinSekarang, Integer
    // id) {
    // UserDetailEntity userEntity = userDetailRepository.findById(id).get();
    // if (userEntity.getPin() == pinSekarang) {
    // return ResponseEntity.ok().body("sama");
    // } else {

    // }
    // return ResponseEntity.ok()
    // .body("Your Bank Account Number Has Been Registered Successfully! " + "Your
    // Bank Account Number : ");
    // }

    // ======Get User======
    @GetMapping("getfriend/{id}")
    public ResponseEntity<?> getFriendById(@PathVariable Integer id) {
        if (userDetailRepository.findById(id).isPresent()) {
            UserDetailEntity userDetailEntity = userDetailRepository.findById(id).get();

            Map<String, Object> userData = new HashMap<>();
            PhoneNumberEntity phoneNumberEntity = phoneRepository.findByUserAndIsPrimary(userDetailEntity, true);
            AccountEntity accountEntity = accountRepository.findByUserId(userDetailEntity);
            userData.put("phoneNumber", phoneNumberEntity.getPhoneNumber());
            userData.put("userId", userDetailEntity.getUserId());
            userData.put("userName", userDetailEntity.getUsername());
            userData.put("userImage", userDetailEntity.getUserImage());
            userData.put("userEmail", userDetailEntity.getEmail());
            userData.put("userPin", userDetailEntity.getPin());
            userData.put("accountId", accountEntity.getAccountId());
            userData.put("accountBalance", accountEntity.getBalance());

            return ResponseEntity.ok().body(userData);
        }
        return ResponseEntity.badRequest().body("Id not found");
    }

    // ======Check Email - Reset Pass : Login Page======

    @PostMapping("/resetpass")
    public ResponseEntity<?> resetPasswordLogin(@RequestBody UserDetailDto dto) {
        // try {
        UserDetailEntity userDetailEntity = userDetailRepository.findByEmail(dto.getEmail());
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userDetailEntity.getUserId());
        userData.put("userEmail", userDetailEntity.getEmail());
        return ResponseEntity.ok().body(userData);
        // } catch (Exception e) {
        // return ResponseEntity.ok().body("Invalid Email");
        // }
    }

    @GetMapping("/bank")
    public ResponseEntity<?> getBank() {
        StatusMessageDto response = new StatusMessageDto<>();
        try {
            List<UserDetailEntity> listBank = userDetailRepository.findBank();
            response.setMessage("Success");
            response.setStatus(HttpStatus.ACCEPTED.toString());
            response.setData(listBank);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setMessage("Failed");
            response.setStatus(HttpStatus.BAD_REQUEST.toString());
            response.setData("Error");
            return ResponseEntity.ok().body(response);
        }

    }

    @GetMapping("/bank/{bankName}")
    public ResponseEntity<?> getBankByName(@PathVariable(value = "bankName") String bankName) {
        StatusMessageDto response = new StatusMessageDto<>();
        try {
            UserDetailEntity listBank = userDetailRepository.findBankByName(bankName);
            response.setMessage("Success");
            response.setStatus(HttpStatus.ACCEPTED.toString());
            response.setData(listBank);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setMessage("Failed");
            response.setStatus(HttpStatus.BAD_REQUEST.toString());
            response.setData("Error");
            return ResponseEntity.ok().body(response);
        }

    }

    // =====Edit User - Personnal Info=====

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<?> updatePersonalInformation(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        // Integer idInt = numEncryptor.decrypt(id).intValue();
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setUsername(dto.getUsername());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Profile Has Been Changed Successfully!");
    }

    @PutMapping("/updatefname/{id}")
    public ResponseEntity<?> updateFirstName(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setUsername(dto.getUserFname());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your First Name Has Changed Saved Successfully!");
    }

    @PutMapping("/updatelname/{id}")
    public ResponseEntity<?> updateLastName(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setUsername(dto.getUserLname());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Last Name Has Been Changed Successfully!");
    }

    @PutMapping("/update-email/{id}")
    public ResponseEntity<?> updateEmail(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setEmail(dto.getEmail());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Email Has Been Changed Successfully!");
    }

}
