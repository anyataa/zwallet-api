package com.zwallet.zwalletapi.Service;

import java.util.Collection;
import java.util.List;

import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnabled;

    // create build static method for build the user details from user entity
    public static UserDetailsImpl build(UserDetailEntity user) {
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getUserRole());
        user.setActive(true);
        return new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities, user.isActive());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return this.authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return this.isEnabled;
    }

}
