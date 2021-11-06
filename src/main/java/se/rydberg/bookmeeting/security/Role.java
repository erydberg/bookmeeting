package se.rydberg.bookmeeting.security;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
}
