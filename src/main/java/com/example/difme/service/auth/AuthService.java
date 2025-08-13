package com.example.difme.service.auth;

import com.example.difme.model.UserModel;

public interface AuthService {

    UserModel authenticate(String token);

    String login(String userName, String password);
}
