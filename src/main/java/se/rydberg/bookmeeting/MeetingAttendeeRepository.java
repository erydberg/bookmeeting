package se.rydberg.bookmeeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MeetingAttendeeRepository extends JpaRepository<MeetingAttendee, UUID> {
    List<MeetingAttendee> findByMeetingAnswers(MeetingAnswer meetingAnswers);
}
