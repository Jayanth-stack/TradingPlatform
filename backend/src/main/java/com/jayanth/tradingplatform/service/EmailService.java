package com.jayanth.tradingplatform.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationOTPEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        try {
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Verification Email");
            mimeMessageHelper.setText("Your verification code is " + otp, false);
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendException("Failed to send email to " + email + ": " + e.getMessage());
        } catch (MessagingException e) {
            throw new MessagingException("Error creating email message: " + e.getMessage());
        }
    }
}