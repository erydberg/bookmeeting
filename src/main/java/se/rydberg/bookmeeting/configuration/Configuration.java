package se.rydberg.bookmeeting.configuration;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "configuration")
public class Configuration {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;
    private String email;
    private String emailkey;
    private String domain;
    @Column(name = "startdesc", length = 1000)
    private String startDescription;
    @Column(name = "bookdesc", length = 1000)
    private String bookDescription;
    private boolean allowOnlineParticipantForm;
}
