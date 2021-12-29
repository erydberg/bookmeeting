package se.rydberg.bookmeeting.attendee;

import lombok.*;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private List<MeetingAnswer> meetingAnswers = new ArrayList<>();
}
