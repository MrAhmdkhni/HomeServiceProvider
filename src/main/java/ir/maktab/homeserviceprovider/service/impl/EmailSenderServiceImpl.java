package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.service.EmailSenderService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Override
    public SimpleMailMessage createEmail(String toEmail, String confirmationToken, String personType) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setFrom("popularamirhosein@gmail.com");
        if (personType.equals("customer")) {
            mailMessage.setSubject("Complete Customer Registration!");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/signup/confirm-customer/" + confirmationToken);
        } else if (personType.equals("expert")) {
            mailMessage.setSubject("Complete Expert Registration!");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/signup/confirm-expert/" + confirmationToken);
        }
        return mailMessage;
    }
}

