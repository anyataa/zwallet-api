package com.zwallet.zwalletapi.Service;

import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Utils.ExceptionNotFound;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserDetailServiceImpl implements UserDetailService, UserDetailsService {
    @Autowired
    private UserDetailRepository userRepo;

    @Override
    public UserDetailEntity registerUser(UserDetailEntity user) {
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            ResponseEntity.ok().body(e.getMessage());
            return user;
        }

    }

    @Override
    public UserDetailEntity loginUser(String password, String email) throws ExceptionNotFound {
        UserDetailEntity foundUser = userRepo.findByEmail()
                .orElseThrow(() -> new ExceptionNotFound("Cannot find User with email " + email));
        return foundUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetailEntity foundUser = userRepo.findByEmail()
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with email " + email));
        return UserDetailsAccess.build(foundUser);
    }

}
