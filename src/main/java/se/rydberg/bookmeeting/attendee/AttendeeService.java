package se.rydberg.bookmeeting.attendee;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

@Service
public class AttendeeService {
    public static final String MSG_NO_ATTENDEE_FOUND = "Kan inte hitta deltagaren i systemet.";
    private final AttendeeRepository attendeeRepository;
    private final ModelMapper modelMapper;

    public AttendeeService(AttendeeRepository attendeeRepository, ModelMapper modelMapper) {
        this.attendeeRepository = attendeeRepository;
        this.modelMapper = modelMapper;
    }

    public MeetingAttendee save(MeetingAttendee attendee) {
        attendee.setName(trim(attendee.getName()));
        attendee.setEmail(trim(attendee.getEmail()));
        return attendeeRepository.save(attendee);
    }

    public MeetingAttendeeDTO saveDTO(MeetingAttendeeDTO dto) {
        MeetingAttendee attendee = toEntity(dto);
        return toDto(save(attendee));
    }

    public void deleteById(UUID uuid) {
        attendeeRepository.deleteById(uuid);
    }

    public void delete(MeetingAttendee attendee) {
        attendeeRepository.delete(attendee);
    }

    public List<MeetingAttendee> findAll() {
        return attendeeRepository.findAll();
    }

    public MeetingAttendee findBy(UUID uuid) throws NotFoundInDatabaseException {
        return attendeeRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundInDatabaseException("Hittar inte användaren i systemet."));
    }

    public MeetingAttendeeDTO findDTOBy(UUID id) throws NotFoundInDatabaseException {
        MeetingAttendee attendee = findBy(id);
        return toDto(attendee);
    }

    @Transactional(readOnly = true)
    public MeetingAttendeeDTO findByEmailName(String email, String name) throws NotFoundInDatabaseException {
        return toDto(
                attendeeRepository.findByEmailName(email, name)
                        .orElseThrow(() -> new NotFoundInDatabaseException(MSG_NO_ATTENDEE_FOUND)));
    }

    @Transactional(readOnly = true)
    public MeetingAttendeeDTO findByEmailNameDepartment(String email, String name, UUID id)
            throws NotFoundInDatabaseException {
        return toDto(
                attendeeRepository.findByEmailNameDepartment(trim(email), trim(name), id)
                        .orElseThrow(() -> new NotFoundInDatabaseException(MSG_NO_ATTENDEE_FOUND)));
    }

    @Transactional(readOnly = true)
    public List<MeetingAttendeeDTO> findAllByDepartment(UUID id) {
        return attendeeRepository.findAllAttendeeByDepartment(id)
                .stream()
                .map(meetingAttendee -> toDto(meetingAttendee))
                .toList();
    }

    public MeetingAttendee getWithAnswers(UUID id) throws NotFoundInDatabaseException {
        return attendeeRepository.findAttendeeWithAnswers(id)
                .orElseThrow(() -> new NotFoundInDatabaseException(MSG_NO_ATTENDEE_FOUND));
    }

    public MeetingAttendeeDTO getDTOWithAnswers(UUID id) throws NotFoundInDatabaseException {
        return toDto(getWithAnswers(id));
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

    protected String trim(String value) {
        if (StringUtils.isNotEmpty(value)) {
            return value.trim();
        } else {
            return "";
        }
    }
}
