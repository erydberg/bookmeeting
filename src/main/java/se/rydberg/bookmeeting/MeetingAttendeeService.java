package se.rydberg.bookmeeting;

import org.springframework.stereotype.Service;

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
