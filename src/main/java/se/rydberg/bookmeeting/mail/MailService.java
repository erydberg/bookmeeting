package se.rydberg.bookmeeting.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.util.Properties;

import static java.lang.String.format;

public class MailService {
    private final JavaMailSender mailSender;

    public MailService(String sender, String key){
        this.mailSender = setupMailSender(sender, key);
    }

    public void sendMail(String from, String to, String subject, String body) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(body, true);
        };
        mailSender.send(preparator);
        System.out.println(format("Mail sent to: %s", to));
    }

    public JavaMailSender setupMailSender(String email, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
