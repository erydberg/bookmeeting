package se.rydberg.bookmeeting.answer;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.meeting.Meeting;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAnswerDTO {
    private UUID id;
    private boolean coming;
    private MeetingAttendee attendee;
    private Meeting meeting;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
    private boolean attended;
}
