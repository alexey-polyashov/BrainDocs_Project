package com.braindocs.services;

import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.users.UserModel;
import com.braindocs.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserModel findById(Long id){
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Пользователь по id '" + id + "' не найден"));
    }
    public List<UserModel> findAllConfirmed(){
        return userRepository.findByConfirmed(true);
    }
}
