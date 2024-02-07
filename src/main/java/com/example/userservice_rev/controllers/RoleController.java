package com.example.userservice_rev.controllers;

import com.example.userservice_rev.dtos.CreateRoleRequestDto;
import com.example.userservice_rev.models.Role;
import com.example.userservice_rev.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(CreateRoleRequestDto request){
        Role role = roleService.createRole(request.getName());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
