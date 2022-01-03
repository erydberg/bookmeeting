package se.rydberg.bookmeeting.attendee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.rydberg.bookmeeting.answer.MeetingAnswer;

import java.util.List;
import java.util.UUID;

public interface AttendeeRepository extends JpaRepository<MeetingAttendee, UUID> {
    @Query("SELECT attendee FROM MeetingAttendee as attendee where attendee.department.id = :departmentId order by attendee.name asc")
    List<MeetingAttendee> findAllAttendeeByDepartment(UUID departmentId);

    List<MeetingAttendee> findByMeetingAnswers(MeetingAnswer meetingAnswers);

}
