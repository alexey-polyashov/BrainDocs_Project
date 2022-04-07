package com.braindocs.common;

import com.braindocs.models.OptionModel;
import com.braindocs.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class Options {

    @Autowired
    private ServletContext servletContext;
    private String contextPath;

    private OptionService optionService;

    private Integer fileStorageType = 1;
    private String dateFormat;
    private String dateTimeFormat;

    private String mail_smtpHost;
    private String mail_smtpPort;
    private String mail_login;
    private String mail_password;
    private String mail_serviceEmail;
    private Boolean mail_sslUsed;
    private Boolean mail_needAuthentication;

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
        this.optionService = optionService;
        init();
    }

    public void init(){

        Optional<OptionModel> options = optionService.readOptions();
        if (options.isPresent()) {
            OptionModel opt = options.get();
            this.fileStorageType = opt.getFileStorageType();
            this.dateFormat = optionService.getDateFormat();
            this.dateTimeFormat = optionService.getDateTimeFormat();
            this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
            this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            //mail
            this.mail_smtpHost = opt.getMail_smtpHost();
            this.mail_smtpPort = opt.getMail_smtpPort();
            this.mail_login = opt.getMail_login();
            this.mail_password = opt.getMail_password();
            this.mail_serviceEmail = opt.getMail_serviceEmail();
            this.mail_sslUsed = opt.getMail_sslUsed();
            this.mail_needAuthentication = opt.getMail_needAuthentication();
            //--
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

    public LocalDateTime convertStringToDateTime(String dateTime){
        return parseStringtToDate(dateTime, this.dateTimeFormatter);
    }

    public String convertDateTimeToString(LocalDateTime dateTime){
        return convertDateToString(dateTime, this.dateTimeFormatter);
    }

    public Date convertStringToDate(String dateTime){
        return Date.valueOf(
                parseStringtToDate(dateTime, this.dateFormatter).toLocalDate());
    }

    public String convertDateToString(LocalDateTime dateTime){
        return this.dateFormatter.format(dateTime);
    }
    public String convertDateToString(Date dateTime){
        return this.dateFormatter.format(dateTime.toLocalDate());
    }

    private LocalDateTime parseStringtToDate(String dateTime, DateTimeFormatter formater){
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
            //ldt = LocalDateTime.parse(dateTime, formater);
            ldt = LocalDateTime.parse(dateTime + " 00:00:00", getDateTimeFormatter());
        }
        return ldt;
    }

    private String convertDateToString(LocalDateTime dateTime, DateTimeFormatter formater){
        if(dateTime==null){
            return "";
        }
        return formater.format(dateTime);
    }

    public String getMail_smtpHost() {
        return mail_smtpHost;
    }

    public String getMail_smtpPort() {
        return mail_smtpPort;
    }

    public String getMail_login() {
        return mail_login;
    }

    public String getMail_password() {
        return mail_password;
    }

    public String getMail_serviceEmail() {
        return mail_serviceEmail;
    }

    public Boolean getMail_sslUsed() {
        return mail_sslUsed;
    }

    public Boolean getMail_needAuthentication() {
        return mail_needAuthentication;
    }
}
