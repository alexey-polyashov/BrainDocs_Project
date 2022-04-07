package com.braindocs.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
public class OptionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mail_smtp_host")
    private String mail_smtpHost;
    @Column(name = "mail_smtpPort")
    private String mail_smtpPort;
    @Column(name = "mail_login")
    private String mail_login;
    @Column(name = "mail_password")
    private String mail_password;
    @Column(name = "mail_serviceEmail")
    private String mail_serviceEmail;
    @Column(name = "mail_sslUsed")
    private Boolean mail_sslUsed;
    @Column(name = "mail_needAuthentication")
    private Boolean mail_needAuthentication;

    @Column(name = "file_storage_type")
    private Integer fileStorageType;

}
