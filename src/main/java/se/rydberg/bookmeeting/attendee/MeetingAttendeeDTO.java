package se.rydberg.bookmeeting.attendee;

import lombok.*;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.department.Department;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeeDTO {
    private UUID id;
    @NotNull(message = "Du behöver fylla i ett namn")
    private String name;
    @NotEmpty(message = "E-postadress krävs")
    @Email(message = "Du behöver fylla i en korrekt e-postadress")
    private String email;
    private Status status;
    private Department department;
    private Set<MeetingAnswer> meetingAnswers = new LinkedHashSet<>();
}
