package se.rydberg.bookmeeting.meeting;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.rydberg.bookmeeting.BookmeetingApplication;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookmeetingApplication.class)
@ExtendWith(MockitoExtension.class)
public class MeetingTest {

    private MeetingService meetingService;

    @Disabled
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

    }
}
