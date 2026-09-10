package com.books.store.controller;

import com.books.store.dto.LoginRequest;
import com.books.store.dto.LoginResponse;
import com.books.store.entity.User;
import com.books.store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        User registeredUser=userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = userService.login(request);

        return ResponseEntity.ok(response);
    }

}
