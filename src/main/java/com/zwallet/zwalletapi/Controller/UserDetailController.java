package com.zwallet.zwalletapi.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.zwallet.zwalletapi.Config.JWTUtils;
import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Dto.JWTResponse;
import com.zwallet.zwalletapi.Model.Dto.PhoneNumberDto;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Dto.UserDataFilter;
import com.zwallet.zwalletapi.Model.Dto.UserDetailDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.PhoneNumberEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.PhoneNumberRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Service.AccountImp;
import com.zwallet.zwalletapi.Service.AccountService;
import com.zwallet.zwalletapi.Service.UserDetailsImpl;
import com.zwallet.zwalletapi.Service.UserServiceImpl;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

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
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PhoneNumberRepository phoneRepository;

    @Autowired
    private AccountRepository accountRepository;

    // ======================================Add Vendor(Merchant &
    // Bank)===================================

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDetailDto dto) {
        UserDetailEntity userDetailEntity = new UserDetailEntity();
        userDetailEntity.setUsername(dto.getUsername());
        userDetailEntity.setEmail(dto.getEmail());
        userDetailEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDetailEntity.setUserRole(dto.getUserRole());

        userDetailRepository.save(userDetailEntity);

        return ResponseEntity.ok().body("Success!");
    }

    // ======================================Get
    // Users==============================================

    @GetMapping("/all")
    public ResponseEntity<?> getUsers() {
        List<UserDetailEntity> userDetailEntity = userDetailRepository.findAll();

        return ResponseEntity.ok().body(userDetailEntity);
    }

    // ===============================================Sign
    // Up===================================================

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

            // User Filter
            UserDataFilter dataFilter = new UserDataFilter(dto.getPhoneNumber(), userCreated.getUserId(),
                    dto.getUsername(), null, dto.getEmail(), null);

            // save to repo
            // userService.createUser(userCreated);
            newAccount.setUser(userCreated);
            accountService.postAccount(newAccount);

            PhoneNumberEntity phone = new PhoneNumberEntity();
            phone.setPhoneNumber(dto.getPhoneNumber());
            phone.setPrimary(true);
            phone.setUser(userCreated);
            phoneRepository.save(phone);

            response.setStatus(HttpStatus.CREATED.toString());
            response.setMessage("User created!");
            // response.setData(userCreated);
            response.setData(dataFilter);

            return ResponseEntity.status(HttpStatus.CREATED).body(dataFilter);
        } catch (Exception e) {
            // TODO: handle exception
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessage("Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // =================================================Sign
    // In================================================

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
            userData.put("phonenumber", phoneNumberEntity.getPhoneNumber());
            userData.put("userId", userDetailEntity.getUserId());
            userData.put("userName", userDetailEntity.getUsername());
            userData.put("userImage", userDetailEntity.getUserImage());
            userData.put("userEmail", userDetailEntity.getEmail());
            userData.put("userPin", userDetailEntity.getPin());
            // userData.put("account", accountEntity);

            response.setStatus(HttpStatus.OK.toString());
            response.setMessage("Login success!");
            response.setData(new JWTResponse(jwt, userDetailsImpl.getUsername(), userRoles, userData));
            // response.setData(new JWTResponse(jwt, userDetailsImpl.getUsername(),
            // userRoles, dataFilter));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // TODO: handle exception
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessage("Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==========================================================Soft
    // Delete==================================================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setDeleted(true);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body(userEntity);
    }

    // =============================================Update User Status When Sign
    // In==========================================

    @PutMapping("/signin-status/{id}")
    public ResponseEntity<?> updateSignStatus(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setActive(true);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Login");
    }

    // =============================================Update User Status When Sign
    // Out==============================================

    @PutMapping("/signout-status/{id}")
    public ResponseEntity<?> updateSignOutStatus(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setActive(false);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Logout");
    }

    // =========================================Change
    // Password====================================================

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> updatePassword(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Password Changed!");
    }

    // ================================================Create & Change
    // PIN====================================================

    @PutMapping("/update-pin/{id}")
    public ResponseEntity<?> updatePin(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setPin(dto.getPin());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body(userEntity);
    }

    // ===============================================Reset
    // Password====================================================

    @PutMapping("/reset-password/{email}")
    public ResponseEntity<?> resetPassword(@RequestBody UserDetailDto dto, @PathVariable String email) {
        UserDetailEntity userEntity = userDetailRepository.findByEmail(email);
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Password Has Been Changed Successfully!");
    }

    // =================================================Personal
    // Information==============================================

    @PutMapping("/personalinfo/{id}")
    public ResponseEntity<?> addPersonalInformation(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setUserFname(dto.getUserFname());
        userEntity.setUserLname(dto.getUserLname());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Profile Has Been Saved Successfully!");
    }

    // =======================================================Bank
    // Number=================================================

    @PutMapping("/banknumber/{id}")
    public ResponseEntity<?> addBankNumber(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setBankNumber(dto.getBankNumber());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Bank Account Number Has Been Registered Successfully! "
                + "Your Bank Account Number : " + dto.getBankNumber());
    }

    @GetMapping("/pin")
    // Low : Secure
    // buat 1 dtoChangePin , yang ngirim 2 (pinSekarang, id)
    public ResponseEntity<?> checkPin(@RequestBody String pinSekarang, Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        if (userEntity.getPin() == pinSekarang) {
            return ResponseEntity.ok().body("sama");
        } else {

        }
        return ResponseEntity.ok()
                .body("Your Bank Account Number Has Been Registered Successfully! " + "Your Bank Account Number : ");
    }

}
