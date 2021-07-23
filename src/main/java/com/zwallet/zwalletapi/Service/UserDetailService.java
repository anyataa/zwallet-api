package com.zwallet.zwalletapi.Service;

import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Utils.ExceptionNotFound;

public interface UserDetailService {
    UserDetailEntity registerUser(UserDetailEntity user);

    UserDetailEntity loginUser(String password, String email) throws ExceptionNotFound;
}
