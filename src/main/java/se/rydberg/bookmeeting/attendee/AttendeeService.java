package se.rydberg.bookmeeting.attendee;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public MeetingAttendee findBy(UUID uuid) throws NotFoundInDatabaseException {
        return attendeeRepository.findById(uuid).orElseThrow(()-> new NotFoundInDatabaseException("Hittar inte användaren i systemet."));
    }

    public List<MeetingAttendeeDTO> findAllByDepartment(UUID id) {
        List<MeetingAttendee> attendees = attendeeRepository.findAllAttendeeByDepartment(id);
        return attendees.stream().map(attendee -> toDto(attendee)).collect(Collectors.toList());
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
