package com.braindocs.controllers.users;

import com.braindocs.dto.users.NewUserDTO;
import com.braindocs.dto.users.UserDTO;
import com.braindocs.exceptions.AnyOtherException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.users.UserService;
import jdk.internal.org.objectweb.asm.tree.analysis.AnalyzerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Any;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService usersService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @RequestBody NewUserDTO userData){

        UserModel user = modelMapper.map(userData, UserModel.class);
        //usersService.createUser(user);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping(value = "/authorized")
    @ResponseBody
    public UserDTO getData(Principal principal) {
        if(principal==null){
            throw new AnyOtherException("Пользователь не авторизован");
        }
        String userName = principal.getName();
        UserModel user = usersService.findByUsername(userName).orElseThrow(()->new ResourceNotFoundException("User with login '" + userName + "' not found!"));
        return modelMapper.map(user, UserDTO.class);
    }

}
