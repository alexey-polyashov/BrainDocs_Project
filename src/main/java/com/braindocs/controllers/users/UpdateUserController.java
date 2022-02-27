package com.braindocs.controllers.users;

import com.braindocs.dto.users.UserDTO;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.mappers.UserMapper;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;

@RestController
@RequestMapping(value = "/api/v1/users/update/{id}")
@RequiredArgsConstructor
public class UpdateUserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PutMapping
    public UserModel updateUser(@RequestBody UserDTO updateRequest) throws ParseException {
        UserModel user = userMapper.toModel(updateRequest);
        return userService.update(user);
    }
}
