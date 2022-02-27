package com.braindocs.controllers.users;

import com.braindocs.models.users.UserModel;
import com.braindocs.services.mappers.UserMapper;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ConfirmedController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/api/v1/noconfirmed")
    public List<UserModel> findNoConfirmedUsers() {
        return userService.findNoConfirmedUsers();
    }

    @GetMapping("/api/v1/confirmed/{id}")
    public UserModel findUserById(@PathVariable Long id) {
        UserModel user = userService.findById(id);
        return userService.confirmedIsTrue(user);
    }
}
