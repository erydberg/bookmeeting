package se.rydberg.bookmeeting.answer;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingAnswerRepository extends JpaRepository<MeetingAnswer, UUID> {
    @Query("SELECT a FROM MeetingAnswer as a WHERE a.attendee.id = (:attendeeid) and a.meeting.id = (:meetingid)")
    Optional<MeetingAnswer> getAnswerForAttendeeAndMeeting(@Param("attendeeid") UUID attendeeid, @Param("meetingid") UUID meetingid);
}
