package com.braindocs.common;

import com.braindocs.models.OptionModel;
import com.braindocs.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.Optional;

@Component
public class Options {

    private final OptionService optionService;

    @Autowired
    private ServletContext servletContext;
    private String contextPath;

    private Integer fileStorageType = 1;
    private String dateFormat;

    @Autowired
    public Options(OptionService optionService) {
        this.optionService = optionService;
        Optional<OptionModel> options = optionService.readOptions();
        if(options.isPresent()){
            OptionModel opt = options.get();
            this.fileStorageType = opt.getFileStorageType();
            this.dateFormat = optionService.getDateFormat();
        }
    }

    @PostConstruct
    public void showIt() {
        this.contextPath = servletContext.getContextPath();
    }

    public Integer getFileStorageType() {
        return fileStorageType;
    }

    public String getDateFormat() {
        return dateFormat;
    }


}
