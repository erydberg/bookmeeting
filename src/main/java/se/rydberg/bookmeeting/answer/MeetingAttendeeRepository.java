package se.rydberg.bookmeeting.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;

import java.util.List;
import java.util.UUID;

public interface MeetingAttendeeRepository extends JpaRepository<MeetingAttendee, UUID> {
    List<MeetingAttendee> findByMeetingAnswers(MeetingAnswer meetingAnswers);
}
