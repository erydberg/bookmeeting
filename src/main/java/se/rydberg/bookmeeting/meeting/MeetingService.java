package se.rydberg.bookmeeting.meeting;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ModelMapper modelMapper;

    public MeetingService(MeetingRepository meetingRepository, ModelMapper modelMapper) {
        this.meetingRepository = meetingRepository;
        this.modelMapper = modelMapper;
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

    protected MeetingDTO toDto(Meeting entity) {
        if (entity != null) {
            return modelMapper.map(entity, MeetingDTO.class);
        }else{
            return null;
        }
    }

    protected Meeting toEntity(MeetingDTO dto){
        if (dto != null) {
            return modelMapper.map(dto, Meeting.class);
        }else{
            return null;
        }
    }
}
