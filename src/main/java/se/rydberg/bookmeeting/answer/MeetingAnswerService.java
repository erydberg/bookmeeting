package se.rydberg.bookmeeting.answer;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeetingAnswerService {
    private final MeetingAnswerRepository meetingAnswerRepository;

    public MeetingAnswerService(MeetingAnswerRepository meetingAnswerRepository) {
        this.meetingAnswerRepository = meetingAnswerRepository;
    }

    public MeetingAnswer save(MeetingAnswer answer){
        return meetingAnswerRepository.save(answer);
    }

    public MeetingAnswer getBy(UUID uuid){
        return meetingAnswerRepository.getById(uuid);
    }
}
