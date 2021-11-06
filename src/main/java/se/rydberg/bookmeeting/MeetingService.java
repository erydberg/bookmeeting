package se.rydberg.bookmeeting;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public Meeting save(Meeting meeting){
        return meetingRepository.save(meeting);
    }

    public Meeting getBy(UUID uuid){
        return meetingRepository.getById(uuid);
    }

    public Meeting getWithAnswersBy(UUID uuid){
        return meetingRepository.getMeetingWithAnswers(uuid);
    }

    public void delete(Meeting meeting){
        meetingRepository.delete(meeting);
    }

    public List<Meeting> getAll(){
        return meetingRepository.findAll(Sort.by(Sort.Direction.ASC, "startDateTime"));
    }
}
