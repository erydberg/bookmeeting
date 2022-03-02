package se.rydberg.bookmeeting.mail;

import static java.lang.String.format;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;

public class MailService {
    private final JavaMailSender mailSender;
    private final String sender;
    private final String domainurl;

    public MailService(String sender, String key, String domainurl) {
        this.mailSender = setupMailSender(sender, key);
        this.sender = sender;
        this.domainurl = domainurl;
    }

    public void sendDepartmentMail(DepartmentMail departmentMail) {
        departmentMail.getDepartment()
                .getAttendees().stream()
                .filter(attendee -> attendee.getStatus().equals(Status.ACTIVE))
                .forEach(
                        attendee -> send(
                                sender,
                                attendee.getEmail(),
                                departmentMail.getSubject(),
                                addLinkToRegister(attendee, departmentMail.formattedDescription())));
    }

    private String addLinkToRegister(MeetingAttendee attendee, String text) {
        return text + "<p><a href=\"" + domainurl + "bookmeeting/attendee/" + attendee.getId() + "\">Fyll i om du kommer eller inte</a>";
    }

    public void send(String from, String to, String subject, String body) {
        try {
            MimeMessagePreparator preparator = mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(from);
                message.setSubject(subject);
                message.setText(body, true);
            };
            Thread.sleep(10);
            mailSender.send(preparator);
            System.out.println(format("Mail sent to: %s", to));
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public JavaMailSender setupMailSender(String email, String password) {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost("smtp.gmail.com");
        mailSenderImpl.setPort(587);

        mailSenderImpl.setUsername(email);
        mailSenderImpl.setPassword(password);

        Properties props = mailSenderImpl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSenderImpl;
    }
}
