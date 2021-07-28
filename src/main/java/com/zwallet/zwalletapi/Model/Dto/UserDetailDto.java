package com.zwallet.zwalletapi.Model.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetailDto extends PhoneNumberDto{
    private String username;
    private String email;
    private String password;
    private String pin;
    private String userFname;
    private String userLname;
    private String userImage;
    private String bankNumber;
    private String userRole;
}
