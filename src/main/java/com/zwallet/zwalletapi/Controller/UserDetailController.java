package com.zwallet.zwalletapi.Controller;

import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.UserDetailDto;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

        userDetailRepository.save(userDetailEntity);

        return ResponseEntity.ok().body("Success");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsers() {
        List<UserDetailEntity> userDetailEntity = userDetailRepository.findAll();

        return ResponseEntity.ok().body(userDetailEntity);
    }
}
