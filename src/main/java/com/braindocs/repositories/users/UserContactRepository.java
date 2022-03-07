package com.braindocs.repositories.users;

import com.braindocs.models.users.UserContactModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactRepository extends JpaRepository<UserContactModel, Long> {

    void deleteByUserid(Long userid);

}
