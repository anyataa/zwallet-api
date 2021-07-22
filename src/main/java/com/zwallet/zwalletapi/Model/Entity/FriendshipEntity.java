package com.zwallet.zwalletapi.Model.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friendship_table")
@Data
@NoArgsConstructor
public class FriendshipEntity {
  // @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  // private Integer friendshipId;
  
  // @ManyToOne
  // @JoinColumn(name = "user_id")
  // private UserDetailEntity user;

  // @ManyToOne
  // @JoinColumn(name = "friend_id")
  // private UserDetailEntity friend;

}
