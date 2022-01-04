package se.rydberg.bookmeeting.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    @Query("SELECT d FROM Department d left JOIN FETCH d.attendees WHERE d.id = (:id)")
    Department getDepartmentWithAttendees(@Param("id") UUID id);

    @Query("SELECT d FROM Department d left JOIN FETCH d.meetings WHERE d.id = (:id)")
    Department getDepartmentWithMeetings(@Param("id") UUID id);

    @Query("SELECT d FROM Department d left JOIN FETCH d.attendees left JOIN FETCH d.meetings WHERE d.id = (:id)")
    Department getFullDepartment(@Param("id") UUID id);


}
