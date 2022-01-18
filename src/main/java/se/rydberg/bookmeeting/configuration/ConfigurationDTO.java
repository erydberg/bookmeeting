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
    private String emailpassword;
}
