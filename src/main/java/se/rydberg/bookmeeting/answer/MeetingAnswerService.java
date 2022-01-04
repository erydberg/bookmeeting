package se.rydberg.bookmeeting.answer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeetingAnswerService {
    private final MeetingAnswerRepository meetingAnswerRepository;
    private final ModelMapper modelMapper;

    public MeetingAnswerService(MeetingAnswerRepository meetingAnswerRepository, ModelMapper modelMapper) {
        this.meetingAnswerRepository = meetingAnswerRepository;
        this.modelMapper = modelMapper;
    }

    public MeetingAnswer save(MeetingAnswer answer) {
        return meetingAnswerRepository.save(answer);
    }

    public MeetingAnswer getBy(UUID uuid) {
        return meetingAnswerRepository.getById(uuid);
    }

    public MeetingAnswer toEntity(MeetingAnswerDTO dto){
        if (dto != null) {
            return modelMapper.map(dto, MeetingAnswer.class);
        }
        return null;
    }

    public MeetingAnswerDTO toDto(MeetingAnswer entity){
        if (entity != null) {
            return modelMapper.map(entity, MeetingAnswerDTO.class);
        }
        return null;
    }
}
