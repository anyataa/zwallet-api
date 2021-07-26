package com.zwallet.zwalletapi.Service;

import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
// implement user service and user details service to this class
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserDetailRepository userRepository;

    @Override
    public UserDetailEntity createUser(UserDetailEntity user) {
        // TODO Auto-generated method stub
        return userRepository.save(user);
    }

    @Override
    public UserDetailEntity loginUser(String email, String password) {
        // TODO Auto-generated method stub
        // checked user
        UserDetailEntity user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        // check user
        UserDetailEntity user = userRepository.findByEmail(email);
        // if the user not registered yet
        if (user == null) {
            throw new UsernameNotFoundException("user not found!");
        }
        // jika user ada, disimpan sebagai user detailsnya
        return UserDetailsImpl.build(user);
    }
}
