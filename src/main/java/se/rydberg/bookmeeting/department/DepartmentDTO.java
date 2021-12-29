package se.rydberg.bookmeeting.department;

import lombok.*;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.meeting.Meeting;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {
    private UUID id;
    private String name;
    private List<Meeting> meetings;
    private List<MeetingAttendee> attendees;
}
