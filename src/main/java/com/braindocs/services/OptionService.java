package com.braindocs.services;

import com.braindocs.models.OptionModel;
import com.braindocs.repositories.OptionRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepositories optionRepositories;

    private final String dateFormat = "yyyy-MM-dd";
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm";

    public Optional<OptionModel> readOptions() {
        return optionRepositories.findById(1);
    }

    public String getDateFormat() {
        return dateFormat;
    }
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }
}
