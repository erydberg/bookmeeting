package se.rydberg.bookmeeting.security;

import javax.validation.constraints.NotEmpty;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    @NotEmpty(message = "Fyll i användarnamn")
    private String username;
    @NotEmpty(message = "Fyll i ett lösenord")
    private String password;
    private boolean enabled;
}
