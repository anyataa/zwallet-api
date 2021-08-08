package com.zwallet.zwalletapi.Controller;

import java.util.ArrayList;
import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.FriendshipDto;
import com.zwallet.zwalletapi.Model.Dto.FriendshipItemDto;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Entity.FriendshipEntity;
import com.zwallet.zwalletapi.Model.Entity.PhoneNumberEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.FriendshipRepository;
import com.zwallet.zwalletapi.Repository.PhoneNumberRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    List<FriendshipEntity> friendship = friendshipRepository.findByUser(userDetail);
    List<FriendshipEntity> friends = friendshipRepository.findByUser(userDetail);

    List<FriendshipItemDto> friendItems = new ArrayList<>();

    
    for(int i = 0; i < friends.size(); i++){
      PhoneNumberEntity phoneNumber = phoneNumberRepository.findByUserAndIsPrimary(friendship.get(i).getFriend(), true);
      FriendshipItemDto newFriends = new FriendshipItemDto(friends.get(i).getFriend().getUserId(), userDetail.getUserId(), friends.get(i).getFriend().getUsername(), phoneNumber.getUser().getUserImage(), phoneNumber.getPhoneNumber());

      friendItems.add(newFriends);
    }

    return ResponseEntity.ok().body(friendItems);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addFriends(@RequestBody FriendshipDto dto) throws ResourceNotFoundException {
    StatusMessageDto response = new StatusMessageDto<>();
    FriendshipEntity friend = new FriendshipEntity();
    UserDetailEntity user = userDetailRepository.findById(dto.getUserId())
        .orElseThrow(() -> new ResourceNotFoundException("Sorry, cannot find user with this id"));
    UserDetailEntity newFriend = userDetailRepository.findById(dto.getFriendId())
        .orElseThrow(() -> new ResourceNotFoundException("Sorry, cannot find friend with this id"));
    // check friendhip status
    friend.setUser(user);
    friend.setFriend(newFriend);
    List<FriendshipEntity> checkUserFriend = friendshipRepository.findFriendshipStatus(user, newFriend);
    // .orElse(friendshipRepository.save(friend));
    if (checkUserFriend.size() > 0) {
      response.setData(checkUserFriend);
      response.setMessage("Friends Already added");
      response.setStatus(HttpStatus.OK.toString());
      return ResponseEntity.ok().body(response);
    }
    try {
      friendshipRepository.save(friend);
      response.setData(friend);
      response.setMessage("Success Add Friends");
      response.setStatus(HttpStatus.OK.toString());
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      response.setData(e);
      response.setMessage("An Error Occured");
      response.setStatus(HttpStatus.OK.toString());
      return ResponseEntity.ok().body(response);
    }

  }
}
