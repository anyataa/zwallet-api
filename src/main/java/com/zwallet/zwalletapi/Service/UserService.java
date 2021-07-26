package com.zwallet.zwalletapi.Service;

import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;

public interface UserService {
    UserDetailEntity createUser(UserDetailEntity user);

    UserDetailEntity loginUser(String email, String password);
}
