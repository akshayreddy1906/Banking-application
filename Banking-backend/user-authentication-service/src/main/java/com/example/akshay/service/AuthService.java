package com.example.akshay.service;

import com.example.akshay.dto.SignupRequest;
import com.example.akshay.dto.UserDto;

public interface AuthService {
    UserDto signupUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
}
