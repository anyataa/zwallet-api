package com.zwallet.zwalletapi.Model.Dto;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JWTResponse {
    private String token;
    private String email;
    private Set<String> userRole;
    private String type = "Bearer";

    public JWTResponse(String token, String email, Set<String> userRole) {
        this.token = token;
        this.email = email;
        this.userRole = userRole;
    }
}