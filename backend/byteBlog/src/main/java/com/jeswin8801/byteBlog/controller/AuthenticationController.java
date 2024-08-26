package com.jeswin8801.byteBlog.controller;

import com.jeswin8801.byteBlog.entities.dto.auth.LoginRequestDto;
import com.jeswin8801.byteBlog.entities.dto.auth.RegisterUserRequestDto;
import com.jeswin8801.byteBlog.service.auth.abstracts.AuthenticationService;
import com.jeswin8801.byteBlog.service.webapp.user.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto requestDto) {
        return new ResponseEntity<>(
                authenticationService.loginUser(requestDto),
                HttpStatus.OK
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequestDto requestDto) {
        authenticationService.registerUser(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
