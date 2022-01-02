package se.rydberg.bookmeeting;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.answer.MeetingAnswerService;
import se.rydberg.bookmeeting.answer.MeetingAttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;
import se.rydberg.bookmeeting.meeting.MeetingService;


import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookmeetingApplication.class)
public class RelationsTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingAnswerService answerService;

    @Autowired
    private MeetingAttendeeService attendeeService;

    @Autowired
    private DepartmentService departmentService;



    @Test
    public void shouldLoadRelationsInDb() throws NotFoundInDatabaseException {
        Meeting meeting = new Meeting();
        meetingService.save(meeting);
        UUID id = meeting.getId();
        System.out.println("id: " + id);
        Meeting meeting1 = meetingService.findBy(id);
        System.out.println(meeting1.getId());
    }

    @Test
    public void shouldCreateBooking() throws NotFoundInDatabaseException {
        Department department = Department.builder().name("Spårarna").build();
        Department departmentSaved = departmentService.save(department);

        Meeting meeting = Meeting.builder().title("Möte 1").startDate(LocalDate.now()).build();
        departmentSaved.addMeeting(meeting);
        Meeting meetingSaved = meetingService.save(meeting);
        UUID meetingId = meetingSaved.getId();
        assertThat(meetingSaved.getDepartment().getName()).isEqualTo("Spårarna");

        MeetingAttendee attendee = MeetingAttendee.builder().name("Erik").email("kalle@mail.se").build();
        departmentSaved.addAttendee(attendee);
        attendeeService.save(attendee);
        assertThat(attendee.getDepartment().getName()).isEqualTo("Spårarna");

        MeetingAnswer meetingAnswer = new MeetingAnswer();
        answerService.save(meetingAnswer);
        meetingAnswer.setComing(true);
        meetingAnswer.setAttendee(attendee);

        meeting.addMeetingAnswer(meetingAnswer);
        attendee.addMeetingAnswer(meetingAnswer);

        meetingService.save(meeting);
        attendeeService.save(attendee);

        System.out.println("har antal svar: " + meeting.getMeetingAnswers().size());

        Meeting savedMeetingLazyLoaded = meetingService.findBy(meetingId);
        LazyInitializationException exception = Assertions.assertThrows(
                LazyInitializationException.class,() -> {
                    savedMeetingLazyLoaded.getMeetingAnswers().isEmpty();
        });

        Meeting savedMeeting = meetingService.getWithAnswersBy(meetingId);
        System.out.println(savedMeeting.getId());
        System.out.println("har antal svar: " + savedMeeting.getMeetingAnswers().size());
        savedMeeting.getMeetingAnswers().forEach(MeetingAnswer -> System.out.println(meetingAnswer.getId()));
    }

    @Test
    public void testLazyLoad(){

    }
}
