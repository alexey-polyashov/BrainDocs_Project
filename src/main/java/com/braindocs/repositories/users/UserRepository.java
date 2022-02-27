package com.braindocs.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import com.braindocs.models.users.UserModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByLogin(String username);
    List<UserModel> findByConfirmedIsFalse();
}