package com.example.userservice.Security.Service;

import com.example.userservice.Security.Models.CustomUserDetails;
import com.example.userservice.models.User;
import com.example.userservice.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepo userRepo;
    public CustomUserDetailsService(UserRepo userRepo){

        this.userRepo=userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user=userRepo.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("user not available in db");
        }

        return new CustomUserDetails(user.get());
    }
}
