package com.example.userservice.Services;

import com.example.userservice.Dtos.UserDto;
import com.example.userservice.Exceptions.UserAlreadyExist;
import com.example.userservice.Exceptions.UserNotExist;
import com.example.userservice.models.Session;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.models.User;
import com.example.userservice.repository.SessionRepo;
import com.example.userservice.repository.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepo userRepo;
    private SessionRepo sessionRepo;
    public AuthService(UserRepo userRepo,SessionRepo sessionRepo,BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo=userRepo;
        this.sessionRepo=sessionRepo;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }
    public UserDto signUp(String email,String password) throws UserAlreadyExist {
        Optional<User> userOptional=userRepo.findByEmail(email);
        if(!userOptional.isEmpty()){
            throw new UserAlreadyExist("user with"+email+"already exist.");
        }
        User user=new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser=userRepo.save(user);
        return UserDto.from(savedUser);
    }

    public ResponseEntity<UserDto> signIn(String email, String password) throws UserNotExist{
        Optional<User> userOptional=userRepo.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotExist("user with"+email+"not exist.");
        }
        User user=userOptional.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String token= RandomStringUtils.randomAscii(20);
        MultiValueMapAdapter<String,String> header=new MultiValueMapAdapter<>(new HashMap<>());
        header.add("AUTH_TOKEN" , token);

        Session session=new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepo.save(session);

        UserDto userDto=UserDto.from(user);
        ResponseEntity<UserDto> response=new ResponseEntity<>(
                userDto,
                header,
                HttpStatus.OK
        );
        return response;
    }
    public Optional<UserDto> validateToken(String token, Long userId){
        Optional<Session> sessionOptional=sessionRepo.findByTokenAndUserId(token,userId);
        if(sessionOptional.isEmpty()){
            return Optional.empty();
        }
        Session session=sessionOptional.get();
        if(!session.getSessionStatus().equals(SessionStatus.ACTIVE)){
            return Optional.empty();
        }
        User user=userRepo.findById(userId).get();
        UserDto userDto=UserDto.from(user);
        //if((new Date() - session.getExpiringAt())>0){
        //    return SessionStatus.EXPIRED;
        //}
        return Optional.of(userDto);
    }
    public ResponseEntity<Void> logOut(String token,Long userId){
        Optional<Session> sessionOptional=sessionRepo.findByTokenAndUserId(token,userId);
        if(sessionOptional.isEmpty()){
            return null;
        }
        Session session =sessionOptional.get();
        session.setSessionStatus(SessionStatus.LOGGEDOUT);
        sessionRepo.save(session);
        return ResponseEntity.ok().build();
    }
}
