package com.braindocs.braindocs.services.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.braindocs.braindocs.models.users.UserRoleModel;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.repositories.users.RoleRepository;
import com.braindocs.braindocs.repositories.users.UserRepository;
import java.util.Collections;

@Service(value = "UserServiceV1")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserModel saveUser(UserModel user) {
        UserRoleModel role = roleRepository.findByName("users_roles");
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserModel changePassword(Integer userId, String oldPassword, String newPassword) {
        UserModel user = userRepository.findById(userId).get();
        if (user.getPassword().equals(passwordEncoder.encode(oldPassword)))
            user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public UserModel restorePassword(String email, String newPassword) {
        UserModel user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserModel findByLoginAndPassword(String email, String password) {
        UserModel userEntity = findByEmail(email);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }
}