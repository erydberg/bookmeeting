package se.rydberg.bookmeeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.MeetingNotFoundException;
import se.rydberg.bookmeeting.meeting.MeetingService;


import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookmeetingApplication.class)
public class RelationsTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingAnswerService answerService;

    @Autowired
    private MeetingAttendeeService attendeeService;


    @Test
    public void shouldLoadRelationsInDb() throws InterruptedException, MeetingNotFoundException {
        Meeting meeting = new Meeting();
        meetingService.save(meeting);
        UUID id = meeting.getId();
        System.out.println("id: " + id);
        Meeting meeting1 = meetingService.findBy(id);
        System.out.println(meeting1.getId());
    }

    @Test
    public void shouldCreateBooking(){
        Meeting meeting = Meeting.builder().title("Möte 1").startDate(LocalDate.now()).build();
        meetingService.save(meeting);
        UUID meetingId = meeting.getId();


        MeetingAttendee attendee = MeetingAttendee.builder().name("Erik").email("kalle@mail.se").build();
        attendeeService.save(attendee);

        MeetingAnswer meetingAnswer = new MeetingAnswer();
        answerService.save(meetingAnswer);
        meetingAnswer.setComing(true);
        meetingAnswer.setAttendee(attendee);

        meeting.addMeetingAnswer(meetingAnswer);
        attendee.addMeetingAnswer(meetingAnswer);

        meetingService.save(meeting);
        attendeeService.save(attendee);

        System.out.println("har antal svar: " + meeting.getMeetingAnswers().size());

        Meeting savedMeeting = meetingService.getWithAnswersBy(meetingId);
        System.out.println(savedMeeting.getId());
        System.out.println("har antal svar: " + savedMeeting.getMeetingAnswers().size());
        savedMeeting.getMeetingAnswers().forEach(MeetingAnswer -> System.out.println(meetingAnswer.getId()));
    }
}
