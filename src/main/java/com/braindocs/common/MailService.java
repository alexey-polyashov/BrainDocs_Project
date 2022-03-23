package com.braindocs.common;

import com.braindocs.exceptions.ServiceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final Options options;

    public void sendHTMLEmail(String email, StringBuilder textHTML, String theme) {

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", options.getMail_smtpHost());
        properties.setProperty("mail.smtp.user", options.getMail_login());
        properties.setProperty("mail.smtp.port", options.getMail_smtpPort());
        if(options.getMail_sslUsed()) {
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        //properties.setProperty("mail.debug", "true");
        Session session;
        if(options.getMail_needAuthentication()) {
            session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(options.getMail_login(), options.getMail_password());
                }
            });
        }else{
            session = Session.getDefaultInstance(properties);
        }

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(options.getMail_serviceEmail()));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //copy to admin for debug
            message.addRecipient(Message.RecipientType.CC, new InternetAddress("polyashofff@yandex.ru"));
            // Set Subject: header field
            message.setSubject(theme);
            // Now set the actual message
            message.setContent(textHTML.toString(), "text/html; charset=utf-8");
            //message.setText(textHTML.toString());
            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            log.error(mex.toString());
            //throw new ServiceError("");
        }

    }

}
