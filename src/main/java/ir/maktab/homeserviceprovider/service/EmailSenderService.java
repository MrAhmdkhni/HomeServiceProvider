package ir.maktab.homeserviceprovider.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailSenderService {

    void sendEmail(SimpleMailMessage email);

    SimpleMailMessage createEmail(String toEmail, String confirmationToken, String personType);
}
