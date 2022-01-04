package se.rydberg.bookmeeting.attendee;

import lombok.*;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;

import javax.validation.constraints.Email;
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
    @Email(message = "Du behöver fylla i en korrekt e-postadress")
    private String email;
    private Status status;
    private Set<MeetingAnswer> meetingAnswers = new LinkedHashSet<>();
}
