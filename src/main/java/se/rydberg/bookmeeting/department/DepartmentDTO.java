package se.rydberg.bookmeeting.department;

import lombok.*;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.meeting.Meeting;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {
    private UUID id;
    @NotEmpty (message = "Du behöver ange ett namn på avdelningen.")
    private String name;
    private Set<Meeting> meetings;
    private Set<MeetingAttendee> attendees;
    @Email (message = "E-postadressen behöver vara korrekt.")
    private String departmentEmail;
    private String departmentEmailPassword;
    private String description;

    public String formattedDescription() {
        return description.replaceAll("(\r\n|\n)", "<br>");
    }
}
