package se.rydberg.bookmeeting.answer;

import org.springframework.stereotype.Service;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;

@Service
public class MeetingAttendeeService {
    private final MeetingAttendeeRepository attendeeRepository;

    public MeetingAttendeeService(MeetingAttendeeRepository attendeeRepository) {
        this.attendeeRepository = attendeeRepository;
    }

    public MeetingAttendee save(MeetingAttendee attendee) {
        return attendeeRepository.save(attendee);
    }
}
