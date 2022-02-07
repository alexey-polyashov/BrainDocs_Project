package com.braindocs.braindocs.common;

import com.braindocs.braindocs.models.OptionModel;
import com.braindocs.braindocs.services.OptionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Options {

    private final OptionService optionService;

    private Integer fileStorageType = 1;

    @Autowired
    public Options(OptionService optionService) {
        this.optionService = optionService;
        Optional<OptionModel> options = optionService.readOptions();
        if(options.isPresent()){
            OptionModel opt = options.get();
            this.fileStorageType = opt.getFileStorageType();
        }
    }
}
