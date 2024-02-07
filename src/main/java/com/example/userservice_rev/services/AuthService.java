package com.example.userservice_rev.services;

import com.example.userservice_rev.dtos.UserDto;
import com.example.userservice_rev.models.Session;
import com.example.userservice_rev.models.SessionStatus;
import com.example.userservice_rev.models.User;
import com.example.userservice_rev.repositories.SessionRepository;
import com.example.userservice_rev.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public ResponseEntity<UserDto> login(String email, String password) {
        Optional<User> userOptional =  userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password,user.getPassword())) {
             throw new RuntimeException("Wrong Password");
        }

        String token = RandomStringUtils.randomAlphanumeric(30);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = new UserDto();
        MultiValueMapAdapter<String,String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token" + token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto,headers,HttpStatus.OK);

        return response;
    }

    public ResponseEntity<Void> logout(String token, Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token,userId);
        if(sessionOptional.isEmpty()){
            return null;
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }
    public UserDto signUp(String email, String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser =  userRepository.save(user);

        return UserDto.from(savedUser);
    }

    public SessionStatus validate(String token, Long userId){
        Optional<Session> sessionOptional =  sessionRepository.findByTokenAndUser_Id(token,userId);
        if(sessionOptional.isEmpty()){
            return null;
        }
        return SessionStatus.ACTIVE;
    }

}
