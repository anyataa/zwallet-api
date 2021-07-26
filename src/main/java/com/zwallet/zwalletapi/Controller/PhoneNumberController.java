package com.zwallet.zwalletapi.Controller;

import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.PhoneNumberDto;
import com.zwallet.zwalletapi.Model.Entity.PhoneNumberEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.PhoneNumberRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phone")
public class PhoneNumberController {
  @Autowired
  private PhoneNumberRepository phoneRepository;
  
  @Autowired
  private UserDetailRepository userDetailRepository;

  @GetMapping("/{id}")
  public ResponseEntity<?> getPhones(@PathVariable Integer id) {
    UserDetailEntity userDetail = userDetailRepository.findById(id).get();
    List<PhoneNumberEntity> phones = phoneRepository.findByUser(userDetail);

    return ResponseEntity.ok().body(phones);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addPhone(@RequestBody PhoneNumberDto dto){
    PhoneNumberEntity phone = new PhoneNumberEntity(dto.getPhoneNumber());

    UserDetailEntity user = userDetailRepository.findById(dto.getUserId()).get();

    phone.setUser(user);


    phoneRepository.save(phone);
    return ResponseEntity.ok().body(phone);
  }
}
