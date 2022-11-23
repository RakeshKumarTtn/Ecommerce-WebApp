package com.org.ecom.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(String toEmail,
                                            String subject,
                                            String body){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("amritaps24@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);


        mailSender.send(message);

    }


}
