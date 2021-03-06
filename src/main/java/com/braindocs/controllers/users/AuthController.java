package com.braindocs.controllers.users;

import com.braindocs.configs.JwtTokenUtil;
import com.braindocs.dto.users.JwtRequestDTO;
import com.braindocs.dto.users.JwtResponseDTO;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.exceptions.Violation;
import com.braindocs.services.users.UserService;
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

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDTO authRequest) {
        log.info("createAuthToken, {}", authRequest.getUsername());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            log.info("createAuthToken, Incorrect username or password for user {}", authRequest.getUsername());
            return new ResponseEntity<>(new Violation("", "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        if (Boolean.TRUE.equals(userService.findByLogin(
                        authRequest.getUsername())
                .get()
                .getConfirmed())) {
            UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);
            log.info("createAuthToken, succes - {}", authRequest.getUsername());
            return ResponseEntity.ok(new JwtResponseDTO(token));
        }
        throw new ResourceNotFoundException("?????????????? ???????????? ???????????????????????? ???? ??????????????????????!");
    }

//    @GetMapping("/logout")
//    public Boolean logout(@RequestHeader("Authorization") String token){
//        jwtTokenUtil.saveToken(token);
//        return true;
//    }

}