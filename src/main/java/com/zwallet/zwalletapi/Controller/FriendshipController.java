package com.zwallet.zwalletapi.Controller;

import java.util.ArrayList;
import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.FriendshipDto;
import com.zwallet.zwalletapi.Model.Dto.FriendshipItemDto;
import com.zwallet.zwalletapi.Model.Entity.FriendshipEntity;
import com.zwallet.zwalletapi.Model.Entity.PhoneNumberEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.FriendshipRepository;
import com.zwallet.zwalletapi.Repository.PhoneNumberRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
@CrossOrigin(origins = "http://localhost:3000")
public class FriendshipController {
  @Autowired
  private FriendshipRepository friendshipRepository;

  @Autowired
  private UserDetailRepository userDetailRepository;

  @Autowired
  private PhoneNumberRepository phoneNumberRepository;

  @GetMapping("/{id}")
  public ResponseEntity<?> getFriends(@PathVariable Integer id) {
    UserDetailEntity userDetail = userDetailRepository.findById(id).get();
    PhoneNumberEntity phoneNumber = phoneNumberRepository.findByUserAndIsPrimary(userDetail, true);
    List<FriendshipEntity> friends = friendshipRepository.findByUser(userDetail);
    
    List<FriendshipItemDto> friendItems = new ArrayList<>();

    for(FriendshipEntity item : friends){
      FriendshipItemDto newFriends = new FriendshipItemDto(item.getFriend().getUserId(), userDetail.getUserId(), item.getFriend().getUsername(), userDetail.getUserImage(), phoneNumber.getPhoneNumber());
      friendItems.add(newFriends);
    }

    return ResponseEntity.ok().body(friendItems);
  }


  @PostMapping("/add")
  public ResponseEntity<?> addFriends(@RequestBody FriendshipDto dto) {
    FriendshipEntity friend = new FriendshipEntity();
    UserDetailEntity user = userDetailRepository.findById(dto.getUserId()).get();
    UserDetailEntity newFriend = userDetailRepository.findById(dto.getFriendId()).get();
    friend.setUser(user);
    friend.setFriend(newFriend);
    friendshipRepository.save(friend);
    return ResponseEntity.ok().body("Success");
  }
}
