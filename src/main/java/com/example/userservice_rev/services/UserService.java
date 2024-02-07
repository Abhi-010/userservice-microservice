package com.example.userservice_rev.services;

import com.example.userservice_rev.dtos.UserDto;
import com.example.userservice_rev.models.Role;
import com.example.userservice_rev.models.User;
import com.example.userservice_rev.repositories.RoleRepository;
import com.example.userservice_rev.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    public UserService(UserRepository userRepository,RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        return UserDto.from(user);
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }
}
