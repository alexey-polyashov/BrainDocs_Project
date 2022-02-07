package com.braindocs.braindocs.services.users;

import com.braindocs.braindocs.models.users.UserInfo;

public interface ITokenService {
    String generateToken(UserInfo user);
    UserInfo parseToken(String token);
}