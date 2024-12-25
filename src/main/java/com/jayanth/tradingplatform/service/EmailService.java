package com.jayanth.tradingplatform.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        if (javaMailSender == null) {
            throw new IllegalStateException("JavaMailSender must not be null");
        }
    }

    public void sendVerificationOTPEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        String subject = "Verification Email";

        String text = "Your verification code is " + otp;

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);
        mimeMessageHelper.setTo(email);

        try {
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new MailSendException("Failed to send email: " + e.getMessage());
        }
    }
}