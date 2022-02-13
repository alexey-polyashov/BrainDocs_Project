package com.braindocs.braindocs.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import com.braindocs.braindocs.models.users.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByLogin(String username);
}