package com.zwallet.zwalletapi.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDto {
  public String userId;
  public Integer decUserId;
  public Integer friendId;
}
