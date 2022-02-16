package com.braindocs.braindocs.services.users;

import com.braindocs.braindocs.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.braindocs.braindocs.models.users.UserRoleModel;
import com.braindocs.braindocs.models.users.UserModel;
import com.braindocs.braindocs.repositories.users.RoleRepository;
import com.braindocs.braindocs.repositories.users.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "UserServiceV1")
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<UserRoleModel> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByLogin(username);
    }

    public UserModel saveUser(UserModel user) {
        UserRoleModel role = roleRepository.findByName("users_roles");
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserModel changePassword(Long userId, String oldPassword, String newPassword) {
        UserModel user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Пользователь с id '" + userId + "'"));
        if (user.getPassword().equals(passwordEncoder.encode(oldPassword)))
            user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public UserModel restorePassword(String email, String newPassword) {
        UserModel user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Пользователь с логином '" + email + "'"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserModel findByLoginAndPassword(String email, String password) {
        UserModel userEntity = findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Пользователь с логином '" + email + "'"));
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    public Optional<UserModel> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}