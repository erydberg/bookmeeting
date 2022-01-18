package se.rydberg.bookmeeting.mail;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import se.rydberg.bookmeeting.configuration.ConfigurationDTO;
import se.rydberg.bookmeeting.configuration.ConfigurationService;

import java.util.Properties;

@Configuration
public class GmailConfig {
    private final ConfigurationService configurationService;

    public GmailConfig(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }


    @Bean("gmail")
    public JavaMailSender gmailMailSender() {
        ConfigurationDTO config = configurationService.loadConfiguration();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("etydberg@gmail.om");
        mailSender.setPassword("ddf");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
