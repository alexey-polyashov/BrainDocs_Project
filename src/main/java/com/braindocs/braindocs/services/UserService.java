package com.braindocs.braindocs.services;

import com.braindocs.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserModel findById(Long id){
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Пользователь по id '" + id + "' не найден"));
    }
}
