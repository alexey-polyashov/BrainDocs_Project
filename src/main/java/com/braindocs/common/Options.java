package com.braindocs.common;

import com.braindocs.models.OptionModel;
import com.braindocs.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class Options {

    @Autowired
    private ServletContext servletContext;
    private String contextPath;

    private Integer fileStorageType = 1;
    private String dateFormat;
    private String dateTimeFormat;

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    public Options(OptionService optionService) {
        Optional<OptionModel> options = optionService.readOptions();
        if (options.isPresent()) {
            OptionModel opt = options.get();
            this.fileStorageType = opt.getFileStorageType();
            this.dateFormat = optionService.getDateFormat();
            this.dateTimeFormat = optionService.getDateTimeFormat();
            this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
            this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        }else{
            this.fileStorageType = 1;
            this.dateFormat = optionService.getDateFormat();
            this.dateTimeFormat = optionService.getDateTimeFormat();
        }
        this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
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

    public LocalDateTime converStringtToDateTime(String dateTime){
        LocalDateTime ldt = LocalDateTime.now();
        if(dateTime==null || dateTime.isEmpty()){
            return ldt;
        }
        if(dateTime.indexOf("Z")>=0){
            dateTime = dateTime.replace("Z", "");
        }
        try {
            //допускается универсальный формат
            ldt = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }catch (Exception e) {
            //пробуем наш формат
            ldt = LocalDateTime.parse(dateTime, this.dateTimeFormatter);
        }
        return ldt;
    }

    public String converDateTimeToString(LocalDateTime dateTime){
        if(dateTime==null){
            return "";
        }
        return this.dateTimeFormatter.format(dateTime);
    }

}
