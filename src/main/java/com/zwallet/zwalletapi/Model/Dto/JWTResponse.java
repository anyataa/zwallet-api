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
    private Object user;
    private UserDataFilter filter;

    public JWTResponse(String token, String email, Set<String> userRole) {
        this.token = token;
        this.email = email;
        this.userRole = userRole;
    }

    public JWTResponse(String token, String email, Set<String> userRole, Object user) {
        this.token = token;
        this.email = email;
        this.userRole = userRole;
        this.user = user;
    }

    public JWTResponse(String token, String email, Set<String> userRole, String type, UserDataFilter filter) {
        this.token = token;
        this.email = email;
        this.userRole = userRole;
        this.type = type;
        this.filter = filter;
    }

}