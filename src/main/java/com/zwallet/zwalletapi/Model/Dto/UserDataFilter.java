package com.zwallet.zwalletapi.Model.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDataFilter {
    private String phoneNumber;
    private String userId;
    private String userName;
    private String userImage;
    private String userEmail;
    private String userPin;
    private String userRole;
    private String accountId;
    private Double accountBalance;
    
    public UserDataFilter(String phoneNumber, String userId, String userName, String userImage, String userEmail,
            String userPin, String userRole, String accountId, Double accountBalance) {
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.userEmail = userEmail;
        this.userPin = userPin;
        this.userRole = userRole;
        this.accountId = accountId;
        this.accountBalance = accountBalance;
    }



    // userData.put("phonenumber", phoneNumberEntity.getPhoneNumber());
    // userData.put("userId", userDetailEntity.getUserId());
    // userData.put("userName", userDetailEntity.getUsername());
    // userData.put("userImage", userDetailEntity.getUserImage());
    // userData.put("userEmail", userDetailEntity.getEmail());
    // userData.put("userPin", userDetailEntity.getPin());

}
