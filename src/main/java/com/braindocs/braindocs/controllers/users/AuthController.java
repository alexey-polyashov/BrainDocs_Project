package com.braindocs.braindocs.controllers.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.repositories.users.JdbcRepository;
import com.braindocs.braindocs.services.users.UserService;
import com.braindocs.braindocs.services.users.ITokenService;
import com.braindocs.braindocs.models.users.UserInfo;
import com.braindocs.braindocs.repositories.users.RedisRepository;
import com.braindocs.braindocs.DTO.users.AuthRequestDto;
import com.braindocs.braindocs.DTO.users.AuthResponseDto;
import com.braindocs.braindocs.DTO.users.SignUpRequestDto;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final ITokenService iTokenService;
    private final RedisRepository redisRepository;
    private final JdbcRepository jdbcRepository;

    @GetMapping("/jdbc")
    public UserModel registerUser(@RequestParam String email) {
        return jdbcRepository.getByEmail(email).get();
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody SignUpRequestDto signUpRequest) {
        UserModel user = new UserModel();
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        userService.saveUser(user);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        UserModel user = userService.findByLoginAndPassword(request.getEmail(), request.getPassword());
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));
        UserInfo userInfo = UserInfo.builder()
                .userId(user.getId())
                .userEmail(user.getEmail())
                .role(roles)
                .build();
        String token = iTokenService.generateToken(userInfo);
        return new AuthResponseDto(token);
    }

    @GetMapping("/logout")
    public Boolean logout(@RequestHeader("Authorization") String token){
        redisRepository.saveToken(token);
        return true;
    }
}