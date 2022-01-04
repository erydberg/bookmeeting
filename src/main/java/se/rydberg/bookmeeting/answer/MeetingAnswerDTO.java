package se.rydberg.bookmeeting.answer;

import java.util.UUID;

import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.meeting.Meeting;

public class MeetingAnswerDTO {
    private UUID id;
    private boolean coming;
    private MeetingAttendee attendee;
    private Meeting meeting;

}
