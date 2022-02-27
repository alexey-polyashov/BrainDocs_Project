package com.braindocs.controllers.users;

import com.braindocs.models.users.UserModel;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/noconfirmed")
@RequiredArgsConstructor
public class NoConfirmedController {
    private final UserService userService;

    @GetMapping()
    public List<UserModel> findNoConfirmedUsers() {
        return userService.findNoConfirmedUsers();
    }
}
