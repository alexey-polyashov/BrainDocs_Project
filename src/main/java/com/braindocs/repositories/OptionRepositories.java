package com.braindocs.repositories;

import com.braindocs.models.OptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepositories  extends JpaRepository<OptionModel, Integer> {
}
