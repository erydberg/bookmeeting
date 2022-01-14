package se.rydberg.bookmeeting.meeting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import se.rydberg.bookmeeting.Status;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class MeetingTest {

    @Mock
    private MeetingService meetingService;

    @Mock
    private MeetingRepository meetingRepository;


    @BeforeEach
    public void init(){
        ModelMapper modelMapper = new ModelMapper();
        meetingService = new MeetingService(meetingRepository, modelMapper);
    }

    @Test
    public void shouldTransformEntityToDTO(){
        Meeting meetingEntity = Meeting.builder().title("Ett testmöte")
                .startDate(LocalDate.parse("2022-05-12"))
                .startTime(LocalTime.NOON)
                .endDate(LocalDate.parse("2022-05-12"))
                .endTime(LocalTime.NOON.plusHours(2))
                .build();

        MeetingDTO meetingDTO = meetingService.toDto(meetingEntity);
        assertThat(meetingDTO.getTitle()).isEqualTo(meetingEntity.getTitle());
        assertThat(meetingDTO.getEndDate()).isEqualTo(meetingEntity.getEndDate());
        assertThat(meetingDTO.getStartDate()).isEqualTo(meetingEntity.getStartDate());
        assertThat(meetingDTO.getStartTime()).isEqualTo(meetingEntity.getStartTime());
        assertThat(meetingDTO.getEndTime()).isEqualTo(meetingEntity.getEndTime());
    }

    @Test
    public void shouldTransformDtoToEntity(){
        MeetingDTO dto = MeetingDTO.builder().title("en dto")
                .description("En beskrivning")
                .startDate(LocalDate.parse("2022-05-10"))
                .startTime(LocalTime.NOON)
                .endDate(LocalDate.parse("2022-05-10"))
                .endTime(LocalTime.NOON.plusHours(2)).build();

        Meeting entity = meetingService.toEntity(dto);

        assertThat(entity.getTitle()).isEqualTo(dto.getTitle());
        assertThat(entity.getDescription()).isEqualTo(dto.getDescription());
        assertThat(entity.getStartDate()).isEqualTo(dto.getStartDate());
        assertThat(entity.getStartTime()).isEqualTo(dto.getStartTime());
        assertThat(entity.getEndDate()).isEqualTo(dto.getEndDate());
        assertThat(entity.getEndTime()).isEqualTo(dto.getEndTime());
    }

    @Test
    public void shouldBeBookable(){
        Meeting meeting = Meeting.builder()
                .status(Status.ACTIVE)
                .startDate(LocalDate.now().plusDays(0)).build();
        assertThat(meeting.isBookable()).isTrue();
    }


    @Test
    public void shouldNotBeBookableSinceNotActive(){
        Meeting meeting = Meeting.builder()
                .status(Status.INACTIVE)
                .startDate(LocalDate.parse("2022-02-01")).build();

        assertThat(meeting.isBookable()).isFalse();

    }
}
