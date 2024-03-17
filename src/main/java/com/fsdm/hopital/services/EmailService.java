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
    private String getEmailTemplate(String username , String token){
        String verifyLink = String.format("%s/api/auth/verifyEmail?token=%s", getServerLink(), token);
        String emailTemplate = String.format("<html>\n" +
                "\t<head>\n" +
                "\t\t<style>\n" +
                "\t\t\tbody {\n" +
                "\t\t\t\tfont-family: Arial, sans-serif;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\th1 {\n" +
                "\t\t\t\tcolor: #333333;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\tp {\n" +
                "\t\t\t\tcolor: #666666;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.button {\n" +
                "\t\t\t\tbackground-color: #4caf50;\n" +
                "\t\t\t\tcolor: white;\n" +
                "\t\t\t\tpadding: 10px 20px;\n" +
                "\t\t\t\ttext-decoration: none;\n" +
                "\t\t\t\tborder-radius: 0.5rem;\n" +
                "\t\t\t\ttransition: all 0.3s ease-in-out;\n" +
                "\t\t\t}\n" +
                "\t\t\t.button:hover {\n" +
                "\t\t\t\tfilter: brightness(0.9);\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.link {\n" +
                "\t\t\t\tcolor: #007bff;\n" +
                "\t\t\t\ttext-decoration: none;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.wrapper {\n" +
                "\t\t\t\tdisplay: flex;\n" +
                "\t\t\t\tflex-direction: column;\n" +
                "\t\t\t\talign-items: center;\n" +
                "\t\t\t\tjustify-content: center;\n" +
                "\t\t\t\twidth:  min(300px,90vw);\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t\theight: 600px;\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\t\t\tp {\n" +
                "\t\t\t\tmargin-top: 2rem;\n" +
                "\t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"wrapper\">\n" +
                "\t\t\t<h1>Bonjour Mr(Mlle) %s</h1>\n" +
                "\t\t\t<p>clicker le lien si dessus pour verifier votre email</p>\n" +
                "\t\t\t<a href=\"%s\" class=\"button\">verifier l'email</a>\n" +
                "\t\t\t<p>\n" +
                "\t\t\t\tsi vous ne vous êtes pas inscrit sur notre application, veuillez\n" +
                "\t\t\t\tne pas cliquer sur ce lien\n" +
                "\t\t\t</p>\n" +
                "\t\t\t<a href=\"%s\" class=\"link\">visiter notre site</a>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>\n" , username, verifyLink, getServerLink());
        return emailTemplate;
    }
    public void sendVerificationEmail(String email ,String username, String token){
        sendTemplateEmail(email, "Verify Your Register", username, token);
    }
    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Email sent to " + toEmail);
    }
    @SneakyThrows
    public void sendTemplateEmail(String toEmail, String subject,String username, String token){
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        String htmlContent = getEmailTemplate(username, token);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
