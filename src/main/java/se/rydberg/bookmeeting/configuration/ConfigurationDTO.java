package se.rydberg.bookmeeting.configuration;

import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationDTO {
    private UUID id;
    private String email;
    private String emailkey;
    private String domain;
    private String startDescription;
    private String bookDescription;

    public String formattedStartDescription() {
        return startDescription.replaceAll("(\r\n|\n)", "<br>");
    }

    public String formattedBookDescription() {
        return bookDescription.replaceAll("(\r\n|\n)", "<br>");
    }
}
