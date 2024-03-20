package com.fsdm.hopital.services;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${server.port}")
    private String serverPort;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;
    private final HttpServletRequest request;
    public String getServerLink()
    {
        if(serverPort != null)
            return String.format("%s://%s:%s", request.getScheme(), request.getServerName(), serverPort);
        return String.format("%s://%s", request.getScheme(), request.getServerName());
    }

    public void sendVerificationEmail(String email ,String username, String token){
        String verifyLink = String.format("%s/api/auth/verifyEmail?token=%s", getServerLink(), token);
        String body = "Bonjour Mr (Mlle) " + username + "\n" +
                "clicker le lien si dessus pour verifier votre email\n" +
                verifyLink + "\n" +
                "si vous ne vous êtes pas inscrit sur notre application, veuillez\n" +
                "ne pas cliquer sur ce lien\n" +
                "visiter notre site: " + getServerLink() + "\n";
        sendEmail(email, "Verify Your Register", username, body);
    }
    public void sendEmail(String toEmail, String subject,String username, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
