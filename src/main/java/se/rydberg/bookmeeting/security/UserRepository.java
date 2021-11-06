package se.rydberg.bookmeeting.security;

import org.springframework.data.jpa.repository.JpaRepository;
import se.rydberg.bookmeeting.MeetingAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByMeetingAnswers(MeetingAnswer meetingAnswers);
    Optional<User> findByUsername(String username);
}
