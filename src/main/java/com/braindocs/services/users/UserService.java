package com.braindocs.services.users;

import com.braindocs.configs.JwtTokenUtil;
import com.braindocs.exceptions.BadRequestException;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.users.UserContactModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.models.users.UserRoleModel;
import com.braindocs.repositories.users.RoleRepository;
import com.braindocs.repositories.users.UserContactRepository;
import com.braindocs.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "UserServiceV1")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserContactRepository userContactsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil tokenUtil;

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

    public UserModel findByUsernameOrThrow(String username) {
        return findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? ?????????? ???????? ??????????????????"));
    }

    public UserModel saveNewUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserModel changePassword(Long userId, String oldPassword, String newPassword) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ?? id '" + userId + "'"));
        if (user.getPassword().equals(passwordEncoder.encode(oldPassword)))
            user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public UserModel restorePassword(String email, String newPassword) {
        UserModel user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ?? ?????????????? '" + email + "'"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserModel findByLoginAndPassword(String email, String password) {
        UserModel userEntity = findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ?? ?????????????? '" + email + "'"));
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

    public UserModel findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ???? id '" + id + "' ???? ????????????"));
    }

    public List<UserModel> findByConfirmed(Boolean confirmed) {
        return userRepository.findByConfirmed(confirmed);
    }

    public UserModel confirmUser(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ?? ?????????????? " + id + ""));
        user.setConfirmed(true);
        return userRepository.save(user);
    }

    @Transactional
    public UserModel update(UserModel user) {
        UserModel oldUser = userRepository.findById(
                        user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("???????????????????????? ?? ID " + user.getId() + ""));
        oldUser.setBirthday(user.getBirthday());
        oldUser.setEmail(user.getEmail());
        oldUser.setFullname(user.getFullname());
        oldUser.setLogin(user.getLogin());
        oldUser.setMale(user.getMale());
        oldUser.setShortname(user.getShortname());
        oldUser.setOrganisation(user.getOrganisation());
        List<UserContactModel> userContacts = user.getContacts();
        userContactsRepository.deleteByUserid(user.getId());
        userContacts.stream()
                .forEach(p -> {
                    p.setUserid(oldUser.getId());
                    userContactsRepository.save(p);
                });
//        if(userContacts!=null){
//            for (UserContactModel userContact: userContacts) {
//                userContactsRepository.save(userContact);
//            }
//        }
        return oldUser;
    }
}