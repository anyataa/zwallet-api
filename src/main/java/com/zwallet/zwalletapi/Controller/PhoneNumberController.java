package com.zwallet.zwalletapi.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zwallet.zwalletapi.Model.Dto.PhoneNumberDto;
import com.zwallet.zwalletapi.Model.Entity.PhoneNumberEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.PhoneNumberRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phone")
@CrossOrigin(origins = "http://localhost:3000")
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

  @GetMapping("/number/{number}")
  public ResponseEntity<?> getPhoneByPhoneNumber(@PathVariable String number){
    PhoneNumberEntity phoneNumber = phoneRepository.findByPhoneNumberAndIsPrimary(number, true);

    Map<String, String> newData = new HashMap<>();

    newData.put("userId", phoneNumber.getUser().getUserId().toString());
    newData.put("phoneNumber", phoneNumber.getPhoneNumber());
    newData.put("username", phoneNumber.getUser().getUsername());
    newData.put("userImage", phoneNumber.getUser().getUserImage());

    return ResponseEntity.ok().body(newData);
  }

  @GetMapping("/get-primary/{id}")
  public ResponseEntity<?> getPrimary(@PathVariable Integer id) {
    UserDetailEntity userDetail = userDetailRepository.findById(id).get();

    PhoneNumberEntity phoneNumber = phoneRepository.findByUserAndIsPrimary(userDetail, true);
    
    return ResponseEntity.ok().body(phoneNumber);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addPhone(@RequestBody PhoneNumberDto dto){
    PhoneNumberEntity phone = new PhoneNumberEntity(dto.getPhoneNumber());

    UserDetailEntity user = userDetailRepository.findById(dto.getUserId()).get();

    phone.setUser(user);

    phoneRepository.save(phone);
    return ResponseEntity.ok().body(phone);
  }

  @PutMapping("/set-primary")
  public ResponseEntity<?> updatePhone(@RequestBody PhoneNumberDto dto){
    try {
      UserDetailEntity userDetail = userDetailRepository.findById(dto.getUserId()).get();
      List<PhoneNumberEntity> phones = phoneRepository.findByUser(userDetail);

      for(PhoneNumberEntity e : phones){
        e.setPrimary(false);
        phoneRepository.save(e);
      }

      PhoneNumberEntity phone = phoneRepository.findById(Integer.parseInt(dto.getPhoneNumberId())).get();
  
      phone.setPrimary(true);
  
      phoneRepository.save(phone);
      
      return ResponseEntity.ok().body("Set to primary success");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST.value());
    }
  }


}
