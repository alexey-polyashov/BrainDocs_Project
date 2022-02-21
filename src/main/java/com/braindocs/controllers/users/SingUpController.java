package com.braindocs.controllers.users;

import com.braindocs.dto.users.NewUserDTO;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/singup")
@RequiredArgsConstructor
@Slf4j
public class SingUpController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody NewUserDTO signUpRequest) {
        UserModel user = new UserModel();
        user.setLogin(signUpRequest.getLogin());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setFullname(signUpRequest.getFullname());
        userService.saveUser(user);
    }

}
