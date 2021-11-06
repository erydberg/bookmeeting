package se.rydberg.bookmeeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeetingAnswerRepository extends JpaRepository<MeetingAnswer, UUID> {
}
