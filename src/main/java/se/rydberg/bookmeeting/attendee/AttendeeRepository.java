package se.rydberg.bookmeeting.attendee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendeeRepository extends JpaRepository<MeetingAttendee, UUID> {
    @Query("SELECT attendee FROM MeetingAttendee as attendee where attendee.department.id = :departmentId order by attendee.name asc")
    List<MeetingAttendee> findAllAttendeeByDepartment(UUID departmentId);

    @Query("SELECT attendee FROM MeetingAttendee as attendee left JOIN FETCH attendee.meetingAnswers WHERE attendee.id =(:id)")
    Optional<MeetingAttendee> findAttendeeWithAnswers(@Param("id") UUID id);
}
