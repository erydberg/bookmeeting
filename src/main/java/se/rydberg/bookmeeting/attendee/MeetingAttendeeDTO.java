package se.rydberg.bookmeeting.attendee;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.*;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.department.Department;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeeDTO {
    private UUID id;
    @NotNull(message = "Du behöver fylla i ett namn")
    private String name;
    @Email(message = "Du behöver fylla i en korrekt e-postadress")
    private String email;
    @Email(message = "Du behöver fylla i en korrekt e-postadress")
    private String emailParent1;
    @Email(message = "Du behöver fylla i en korrekt e-postadress")
    private String emailParent2;
    private Status status;
    private Department department;
    private Set<MeetingAnswer> meetingAnswers = new LinkedHashSet<>();
}
