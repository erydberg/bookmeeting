package se.rydberg.bookmeeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.rydberg.bookmeeting.security.User;
import se.rydberg.bookmeeting.security.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookmeetingApplication.class)
public class RelationsTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetingAnswerService answerService;


    @Test
    public void shouldLoadRelationsInDb() throws InterruptedException {
        Meeting meeting = new Meeting();
        meetingService.save(meeting);
        UUID id = meeting.getId();
        System.out.println("id: " + id);
        Meeting meeting1 = meetingService.getBy(id);
        System.out.println(meeting1.getId());
    }

    @Test
    public void shouldCreateBooking(){
        Meeting meeting = Meeting.builder().title("Möte 1").startDateTime(LocalDateTime.now()).build();
        meetingService.save(meeting);
        UUID meetingId = meeting.getId();

        User user = User.builder().username("erik").enabled(true).password("lösen").build();
        userService.save(user);

        MeetingAnswer meetingAnswer = new MeetingAnswer();
        answerService.save(meetingAnswer);
        meetingAnswer.setComing(true);
        meetingAnswer.setUser(user);

        meeting.addMeetingAnswer(meetingAnswer);
        user.addMeetingAnswer(meetingAnswer);

        meetingService.save(meeting);
        userService.save(user);

        System.out.println("har antal svar: " + meeting.getMeetingAnswers().size());

        Meeting savedMeeting = meetingService.getWithAnswersBy(meetingId);
        System.out.println(savedMeeting.getId());
        System.out.println("har antal svar: " + savedMeeting.getMeetingAnswers().size());
        savedMeeting.getMeetingAnswers().forEach(MeetingAnswer -> System.out.println(meetingAnswer.getId()));



    }

    @Test
    public void testSaveUser(){
        User user = User.builder().username("erik").password("lösen").build();
        userService.save(user);
        System.out.println("UserID: " + user.getId());
    }
}
