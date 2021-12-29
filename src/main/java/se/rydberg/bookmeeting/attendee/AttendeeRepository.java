package se.rydberg.bookmeeting.attendee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendeeRepository extends JpaRepository<MeetingAttendee, UUID> {
}
