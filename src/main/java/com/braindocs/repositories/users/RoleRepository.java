package com.braindocs.repositories.users;

import com.braindocs.models.users.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRoleModel, Integer> {
    UserRoleModel findByName(String name);
}