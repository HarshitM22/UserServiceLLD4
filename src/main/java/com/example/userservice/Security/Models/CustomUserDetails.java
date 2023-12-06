package com.example.userservice.Security.Models;

import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@JsonDeserialize
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    //private User user;
    private List<GrantedAuthority> authorities;
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Long userId;
    public CustomUserDetails(User user){

        //this.user=user;
        this.authorities=new ArrayList<>();
        for(Role role:user.getRoles()){
            this.authorities.add(new CustomGrantedAuthority(role));
        }
        this.password=user.getPassword();
        this.username=user.getEmail();
        this.accountNonExpired=true;
        this.accountNonLocked=true;
        this.credentialsNonExpired=true;
        this.enabled=true;
        this.userId=user.getId();
    }
    public Long getUserId(){
        return this.userId;
    }
    public void setUserId(Long userId){
        this.userId=userId;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<CustomGrantedAuthority> list=new ArrayList<>();
//        for(Role role:user.getRoles()){
//            list.add(new CustomGrantedAuthority(role));
//        }
        return this.authorities;
    }

    @Override
    public String getPassword() {
        //return user.getPassword();
        return this.password;
    }

    @Override
    public String getUsername() {
        //return user.getEmail();
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        //return true;
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        //return true;
        return  this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //return true;
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        //return true;
        return this.enabled;
    }
}
