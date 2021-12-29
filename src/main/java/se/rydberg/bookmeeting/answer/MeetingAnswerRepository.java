package se.rydberg.bookmeeting.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import se.rydberg.bookmeeting.answer.MeetingAnswer;

import java.util.UUID;

public interface MeetingAnswerRepository extends JpaRepository<MeetingAnswer, UUID> {
}
