package se.rydberg.bookmeeting.security;

import java.util.Collection;

import javax.persistence.*;

import lombok.*;

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
