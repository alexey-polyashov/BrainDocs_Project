package com.braindocs.services.users;

import com.braindocs.dto.users.UserDTO;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.repositories.users.UserContactRepository;
import com.braindocs.services.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.braindocs.models.users.UserRoleModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.repositories.users.RoleRepository;
import com.braindocs.repositories.users.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "UserServiceV1")
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
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

    public UserModel saveNewUser(UserModel user) {
//        UserRoleModel role = roleRepository.findByName("users_roles");
//        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserModel saveUser(UserModel user) {
        UserModel oldUser = userRepository.findByLogin(
                user.getLogin())
                .orElseThrow(()->new ResourceNotFoundException("Пользователь с логином " + user.getLogin() + ""));
        user.setPassword(oldUser.getPassword());
        user.setConfirmed(oldUser.getConfirmed());
        user.setMarked(oldUser.getMarked());
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

    public UserModel findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Пользователь по id '" + id + "' не найден"));
    }

    public List<UserModel> findByConfirmed(Boolean confirmed){
        return userRepository.findByConfirmed(confirmed);
    }

    public UserModel confirmUser(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Пользователь с логином " + id + ""));
        user.setConfirmed(true);
        return userRepository.save(user);
    }

    public UserModel update(UserModel user) {
        UserModel oldUser = userRepository.findById(
                        user.getId())
                .orElseThrow(()->new ResourceNotFoundException("Пользователь с ID " + user.getId() + ""));
        user.setPassword(oldUser.getPassword());
        UserModel saveUser = userRepository.save(user);
        user.getContacts().stream().forEach(userContactRepository::save);
        return saveUser;
    }
}