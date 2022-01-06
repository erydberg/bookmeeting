package se.rydberg.bookmeeting.attendee;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

@Service
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final ModelMapper modelMapper;

    public AttendeeService(AttendeeRepository attendeeRepository, ModelMapper modelMapper) {
        this.attendeeRepository = attendeeRepository;
        this.modelMapper = modelMapper;
    }

    public MeetingAttendee save(MeetingAttendee attendee) {
        return attendeeRepository.save(attendee);
    }


    public MeetingAttendeeDTO saveDTO(MeetingAttendeeDTO dto) {
        MeetingAttendee attendee = toEntity(dto);
        return toDto(save(attendee));
    }

    public void deleteById(UUID uuid){
        attendeeRepository.deleteById(uuid);
    }

    public List<MeetingAttendee> findAll(){
        return attendeeRepository.findAll();
    }

    public MeetingAttendee findBy(UUID uuid) throws NotFoundInDatabaseException {
        return attendeeRepository.findById(uuid).orElseThrow(()-> new NotFoundInDatabaseException("Hittar inte användaren i systemet."));
    }

    public MeetingAttendeeDTO findDTOBy(UUID id) throws NotFoundInDatabaseException {
        MeetingAttendee attendee = findBy(id);
        return toDto(attendee);
    }


    public List<MeetingAttendee> findAllByDepartment(UUID id) {
        return attendeeRepository.findAllAttendeeByDepartment(id);
    }

    protected MeetingAttendee toEntity(MeetingAttendeeDTO dto) {
        if (dto != null) {
            return modelMapper.map(dto, MeetingAttendee.class);
        } else {
            return null;
        }
    }

    protected MeetingAttendeeDTO toDto(MeetingAttendee entity) {
        if (entity != null) {
            return modelMapper.map(entity, MeetingAttendeeDTO.class);
        } else {
            return null;
        }
    }
}
