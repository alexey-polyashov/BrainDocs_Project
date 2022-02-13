package com.braindocs.braindocs.controllers.users;

import com.braindocs.braindocs.DTO.users.NewUserDto;
import com.braindocs.braindocs.DTO.users.UserDTO;
import com.braindocs.braindocs.exception.ResourceNotFoundException;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/users/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService usersService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @RequestBody NewUserDto userData){

        UserModel user = modelMapper.map(userData, UserModel.class);
        //usersService.createUser(user);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping(value = "/userdata")
    @ResponseBody
    public UserDTO getData(Principal principal) {
        String userName = principal.getName();
        UserModel user = usersService.findByUsername(userName).orElseThrow(()->new ResourceNotFoundException("User with login '" + userName + "' not found!"));
        return modelMapper.map(user, UserDTO.class);
    }

}
