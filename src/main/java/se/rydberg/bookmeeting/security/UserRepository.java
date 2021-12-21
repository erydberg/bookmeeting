package se.rydberg.bookmeeting.security;

import org.springframework.data.jpa.repository.JpaRepository;
import se.rydberg.bookmeeting.MeetingAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
