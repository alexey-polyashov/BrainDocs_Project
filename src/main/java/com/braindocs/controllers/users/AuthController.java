package com.braindocs.controllers.users;

import com.braindocs.dto.users.*;
import com.braindocs.configs.JwtTokenUtil;
import com.braindocs.exceptions.ServiceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.braindocs.services.users.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
//    private final RedisRepository redisRepository;
//    private final JdbcRepository jdbcRepository;

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDTO authRequest) {
        log.info("createAuthToken, {}", authRequest.getUsername());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            log.info("createAuthToken, Incorrect username or password for user {}", authRequest.getUsername());
            return new ResponseEntity<>(new ServiceError("Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        log.info("createAuthToken, succes - {}", authRequest.getUsername());
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

//    @GetMapping("/jdbc")
//    public UserModel registerUser(@RequestParam String email) {
//        return jdbcRepository.getByEmail(email).get();
//    }
//
//    @PostMapping("/signup")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void registerUser(@RequestBody SignUpRequestDto signUpRequest) {
//        UserModel user = new UserModel();
//        user.setPassword(signUpRequest.getPassword());
//        user.setEmail(signUpRequest.getEmail());
//        userService.saveUser(user);
//    }

//    @PostMapping("/login")
//    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
//        UserModel user = userService.findByLoginAndPassword(request.getEmail(), request.getPassword());
//        List<String> roles = new ArrayList<>();
//        user.getRoles().forEach(role -> roles.add(role.getName()));
//        UserInfo userInfo = UserInfo.builder()
//                .userId(user.getId())
//                .userEmail(user.getEmail())
//                .role(roles)
//                .build();
//        String token = iTokenService.generateToken(userInfo);
//        return new AuthResponseDto(token);
//    }

//    @GetMapping("/logout")
//    public Boolean logout(@RequestHeader("Authorization") String token){
//        redisRepository.saveToken(token);
//        return true;
//    }

}