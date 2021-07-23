package com.zwallet.zwalletapi.Controller;

import java.util.List;

import com.zwallet.zwalletapi.Model.Entity.FriendshipEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.FriendshipRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendshipController {
  @Autowired
  private FriendshipRepository friendshipRepository;

  @Autowired
  private UserDetailRepository userDetailRepository;

  @GetMapping("/{id}")
  public ResponseEntity<?> getFriends(@PathVariable Integer id){
    UserDetailEntity userDetail = userDetailRepository.findById(id).get();
    List<FriendshipEntity> friends = friendshipRepository.findByUser(userDetail);

    return ResponseEntity.ok().body(friends);
  }
}
