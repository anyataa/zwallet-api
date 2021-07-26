package com.zwallet.zwalletapi.Controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.zwallet.zwalletapi.Config.JWTUtils;
import com.zwallet.zwalletapi.Model.Dto.JWTResponse;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Dto.UserDetailDto;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;

import com.zwallet.zwalletapi.Service.UserDetailsImpl;
import com.zwallet.zwalletapi.Service.UserServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserDetailController {
    @Autowired
    UserDetailRepository userDetailRepository;


    @Autowired
    AccountService accountService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDetailDto dto) {
        UserDetailEntity userDetailEntity = new UserDetailEntity(dto.getUsername(), dto.getEmail(), dto.getPassword(),
                dto.getPin(), dto.getUserFname(), dto.getUserLname(), dto.getUserImage(), dto.getBankNumber(), "USER");


    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    // @PostMapping("/add")
    // public ResponseEntity<?> addUser(@RequestBody UserDetailDto dto) {
    // UserDetailEntity userDetailEntity = new UserDetailEntity(dto.getUsername(),
    // dto.getEmail(), dto.getPassword(),
    // dto.getPin(), dto.getUserFname(), dto.getUserLname(), dto.getUserImage(),
    // dto.getBankNumber(), "USER");

    // userDetailRepository.save(userDetailEntity);

    // return ResponseEntity.ok().body("Success");
    // }

    // ===============================================================Get Users=======================================================

    @GetMapping("/all")
    public ResponseEntity<?> getUsers() {
        List<UserDetailEntity> userDetailEntity = userDetailRepository.findAll();

        return ResponseEntity.ok().body(userDetailEntity);
    }

    // ===============================================================Sign Up=======================================================

    @PostMapping("/signup")
    public ResponseEntity<?> registrasi(@RequestBody UserDetailDto dto) {
        StatusMessageDto<UserDetailEntity> response = new StatusMessageDto<>();
        // checking user exist or not
        UserDetailEntity user = userDetailRepository.findByEmail(dto.getEmail());
        if (user != null) {
            response.setStatus(HttpStatus.EXPECTATION_FAILED.toString());
            response.setMessage("Email already exist!");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }

        // registering account
        try {
            UserDetailEntity userCreated = new UserDetailEntity(dto.getUsername(), dto.getEmail(),
                    passwordEncoder.encode(dto.getPassword()), dto.getPin(), dto.getUserFname(), dto.getUserLname(),
                    dto.getUserImage(), dto.getBankNumber(), dto.getUserRole());

            // save to repo
            userService.createUser(userCreated);

            response.setStatus(HttpStatus.CREATED.toString());
            response.setMessage("User created!");
            response.setData(userCreated);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // TODO: handle exception
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessage("Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ===============================================================Sign In=======================================================

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

            response.setStatus(HttpStatus.OK.toString());
            response.setMessage("Login success!");
            response.setData(new JWTResponse(jwt, userDetailsImpl.getUsername(), userRoles));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // TODO: handle exception
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setMessage("Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ===============================================================Soft Delete=======================================================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setDeleted(true);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body(userEntity);
    }

    // ===============================================================Update User Status When Sign In=======================================================

    @PutMapping("/signin-status/{id}")
    public ResponseEntity<?> updateSignStatus(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setActive(true);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Login");
    }

    // ===============================================================Update User Status When Sign Out=======================================================

    @PutMapping("/signout-status/{id}")
    public ResponseEntity<?> updateSignOutStatus(@PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setActive(false);
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Logout");
    }

    // ===============================================================Change Password=======================================================

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> updatePassword(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Password Changed!");
    }

    // ===============================================================Change PIN=======================================================

    @PutMapping("/change-pin/{id}")
    public ResponseEntity<?> updatePin(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setPin(dto.getPin());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("PIN Changed!");
    }

    // ===============================================================Reset Password=======================================================

    @PutMapping("/reset-password/{email}")
    public ResponseEntity<?> resetPassword(@RequestBody UserDetailDto dto, @PathVariable String email) {
        UserDetailEntity userEntity = userDetailRepository.findByEmail(email);
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Password Has Been Changed Successfully!");
    }

    // ===============================================================Personal Information=======================================================

    @PutMapping("/personalinfo/{id}")
    public ResponseEntity<?> personalInformation(@RequestBody UserDetailDto dto, @PathVariable Integer id) {
        UserDetailEntity userEntity = userDetailRepository.findById(id).get();
        userEntity.setUserFname(dto.getUserFname());;
        userEntity.setUserLname(dto.getUserLname());
        userDetailRepository.save(userEntity);
        return ResponseEntity.ok().body("Your Profile Has Been Saved Successfully!");
    }
}
