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

    @Query("SELECT attendee FROM MeetingAttendee as attendee where attendee.email =(:email) and lower(attendee.name) like lower(concat('%', :name,'%'))")
    Optional<MeetingAttendee> findByEmailName(@Param("email") String email, @Param("name") String name);

    @Query("SELECT attendee FROM MeetingAttendee as attendee where (attendee.email =(:email) or attendee.emailParent1 =(:email) or attendee.emailParent2 =(:email)) and lower(attendee.name) like lower(concat('%', :name,'%')) and attendee.department.id =(:department)")
    Optional<MeetingAttendee> findByEmailNameDepartment(@Param("email") String email, @Param("name") String name, @Param("department") UUID id);

    //@Query("SELECT attendee FROM MeetingAttendee as attendee where attendee.email =(:email) and lower(attendee.name) like lower(concat('%', :name,'%')) and attendee.department.id =(:department)")
    //Optional<MeetingAttendee> findByEmailNameDepartment(@Param("email") String email, @Param("name") String name, @Param("department") UUID id);


}
