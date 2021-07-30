package com.zwallet.zwalletapi.Model.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDataFilter {
    private String phoneNumber;
    private Integer userId;
    private String userName;
    private String userImage;
    private String userEmail;
    private String userPin;

    public UserDataFilter(String phoneNumber, Integer userId, String userName, String userImage, String userEmail,
            String userPin) {
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.userEmail = userEmail;
        this.userPin = userPin;
    }

    // userData.put("phonenumber", phoneNumberEntity.getPhoneNumber());
    // userData.put("userId", userDetailEntity.getUserId());
    // userData.put("userName", userDetailEntity.getUsername());
    // userData.put("userImage", userDetailEntity.getUserImage());
    // userData.put("userEmail", userDetailEntity.getEmail());
    // userData.put("userPin", userDetailEntity.getPin());

}