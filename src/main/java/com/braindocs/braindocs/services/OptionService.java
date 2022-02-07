package com.braindocs.braindocs.services;

import com.braindocs.braindocs.models.OptionModel;
import com.braindocs.braindocs.repositories.OptionRepositories;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepositories optionRepositories;

    public Optional<OptionModel> readOptions() {
        return optionRepositories.findById(1);
    }

}
