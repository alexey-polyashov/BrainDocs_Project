package com.braindocs.controllers.users;

import com.braindocs.dto.users.NewUserDTO;
import com.braindocs.dto.users.UserDTO;
import com.braindocs.exceptions.AnyOtherException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.users.UserModel;
import com.braindocs.services.mappers.UserMapper;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public Long registerUser(@Valid @RequestBody NewUserDTO signUpRequest) {
        UserModel user = userMapper.toModel(signUpRequest);
        userService.saveNewUser(user);
        return user.getId();
    }

    @GetMapping(value = "/authorized")
    @ResponseBody
    public UserDTO getData(Principal principal) {
        if(principal==null){
            throw new AnyOtherException("Пользователь не авторизован");
        }
        String userName = principal.getName();
        UserModel user = userService.findByUsername(userName).orElseThrow(()->new ResourceNotFoundException("User with login '" + userName + "' not found!"));
        return userMapper.toDTO(user);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@RequestBody UserDTO updateRequest, @PathVariable(name = "id") Long id) throws ParseException {
        if (id == 0) {
            throw new AnyOtherException("ID не должен быть равен нулю");
        }
        updateRequest.setId(id);
        updateRequest.getContacts().stream().forEach(p->p.setUserId(id));
        UserModel user = userMapper.toModel(updateRequest);
        return userMapper.toDTO(userService.update(user));
    }

    @GetMapping()
    public List<UserDTO> findUsers(@RequestParam(name = "confirmed", required = false, defaultValue = "true") Boolean confirmed) {
        return userService.findByConfirmed(confirmed).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/confirm/{id}")
    public UserDTO confirmUser(@PathVariable Long id) {
        return userMapper.toDTO(userService.confirmUser(id));
    }

}
