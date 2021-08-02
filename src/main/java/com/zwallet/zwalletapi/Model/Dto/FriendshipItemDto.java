package com.zwallet.zwalletapi.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipItemDto {
  private Integer friendId;
  private Integer userId;
  private String username;
  private String userImage;
  private String phoneNumber;
}
