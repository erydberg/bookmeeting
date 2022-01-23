package se.rydberg.bookmeeting.bookpublic;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.rydberg.bookmeeting.department.Department;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeReminder {
    @NotEmpty(message = "Du behöver fylla i e-postadress")
    private String email;
    @NotEmpty(message = "Du behöver fylla i deltagarens för och efternamn")
    private String name;
    private Department department;
}
