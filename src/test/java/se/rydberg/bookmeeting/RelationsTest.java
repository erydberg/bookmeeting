package se.rydberg.bookmeeting;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.answer.MeetingAnswerService;
import se.rydberg.bookmeeting.attendee.AttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;
import se.rydberg.bookmeeting.meeting.MeetingService;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookmeetingApplication.class)
public class RelationsTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingAnswerService answerService;

    @Autowired
    private AttendeeService attendeeService;

    @Autowired
    private DepartmentService departmentService;

    Department departmentSparare;
    Department departmentUpptackare;
    Department departmentAventyrare;

    MeetingAttendee attendeeSparare1;
    MeetingAttendee attendeeAventyrare2;
    MeetingAttendee attendeeUpptackare2;
    MeetingAttendee attendeeUpptackare1;
    MeetingAttendee attendeeAventyrare1;
    MeetingAttendee attendeeSparare2;

    Meeting meetingSparare1;
    Meeting meetingSparare2;
    Meeting meetingUpptackare1;
    Meeting meetingUpptackare2;
    Meeting meetingAventyrare1;
    Meeting meetingAventyrare2;

    @BeforeAll
    public void init() throws NotFoundInDatabaseException {
        setupDepartment();
        setupAttendees();
        setupMeetings();
        mapDepartmentAttendees();
    }



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
    public void shouldLoadThreeDepartments(){
        List<Department> departments = departmentService.getAll();
        assertThat(departments).hasSize(3);
    }

    @Test
    public void shouldLoadSixMeetings(){
        List<Meeting> meetings = meetingService.getAll();
        assertThat(meetings).hasSize(6);
    }

    //TODO fixa testet
    @Test
    public void shouldLoadSixAttendees(){
    }

    @Test
    public void departmentShouldHaveAttendees() throws NotFoundInDatabaseException {
        Department fullSparare = departmentService.getDepartmentWithAttendees(departmentSparare.getId());
        System.out.println("Hittat avdelning: " + fullSparare.getName());
        assertThat(fullSparare.getAttendees()).hasSize(2);
    }


    @Test
    public void shouldCreateBooking() throws NotFoundInDatabaseException {
        Meeting meeting = Meeting.builder().title("Möte 1").startDate(LocalDate.now()).build();
        departmentSparare.addMeeting(meeting);
        Meeting meetingSaved = meetingService.save(meeting);
        UUID meetingId = meetingSaved.getId();
        assertThat(meetingSaved.getDepartment().getName()).isEqualTo("Spårarna");

        MeetingAttendee attendee = MeetingAttendee.builder().name("Erik").email("kalle@mail.se").build();
        departmentSparare.addAttendee(attendee);
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

    private void mapDepartmentAttendees() throws NotFoundInDatabaseException {
        MeetingAttendee attendeeSparare1Saved = attendeeService.findBy(attendeeSparare1.getId());
        MeetingAttendee attendeeSparare2Saved = attendeeService.findBy(attendeeSparare2.getId());
        Department departmentSparareSaved = departmentService.getDepartmentWithAttendees(departmentSparare.getId());
        departmentSparareSaved.addAttendee(attendeeSparare1Saved);
        attendeeService.save(attendeeSparare1Saved);
        departmentSparareSaved.addAttendee(attendeeSparare2Saved);
        departmentService.save(departmentSparareSaved);
    }

    private void setupMeetings() {
        System.out.println("Creating six meetings");
        meetingSparare1 = Meeting.builder().title("Möte1 - spårare").startDate(LocalDate.now()).build();
        meetingService.save(meetingSparare1);
        meetingSparare2 = Meeting.builder().title("Möte2 - spårare").startDate(LocalDate.now()).build();
        meetingService.save(meetingSparare2);
        meetingUpptackare1 = Meeting.builder().title("Möte1 - upptäckare").startDate(LocalDate.now()).build();
        meetingService.save(meetingUpptackare1);
        meetingUpptackare2 = Meeting.builder().title("Möte2 - upptäckare").startDate(LocalDate.now()).build();
        meetingService.save(meetingUpptackare2);
        meetingAventyrare1 = Meeting.builder().title("Möte1 - Äventyrare").startDate(LocalDate.now()).build();
        meetingService.save(meetingAventyrare1);
        meetingAventyrare2 = Meeting.builder().title("Möte2 - Äventyrare").startDate(LocalDate.now()).build();
        meetingService.save(meetingAventyrare2);
    }


    private void setupAttendees() {
        System.out.println("Creating six attendees");
        attendeeSparare1 = MeetingAttendee.builder().name("Daniel spårare").build();
        attendeeService.save(attendeeSparare1);
        attendeeSparare2 = MeetingAttendee.builder().name("Agnes spårare").build();
        attendeeService.save(attendeeSparare2);
        attendeeUpptackare1 = MeetingAttendee.builder().name("Kalle upptäckare").department(departmentUpptackare).build();
        attendeeService.save(attendeeUpptackare1);
        attendeeUpptackare2 = MeetingAttendee.builder().name("Lotta upptäckare").department(departmentUpptackare).build();
        attendeeService.save(attendeeUpptackare2);
        attendeeAventyrare1 = MeetingAttendee.builder().name("Nisse äventyrare").department(departmentAventyrare).build();
        attendeeService.save(attendeeAventyrare1);
        attendeeAventyrare2 = MeetingAttendee.builder().name("Stina äventyrare").department(departmentAventyrare).build();
        attendeeService.save(attendeeAventyrare2);
    }

    private void setupDepartment() {
        System.out.println("Creating three departments");
        departmentSparare = Department.builder().name("Spårarna").build();
        departmentService.save(departmentSparare);

        departmentUpptackare = Department.builder().name("Upptäckare").build();
        departmentService.save(departmentUpptackare);

        departmentAventyrare = Department.builder().name("Äventyrare").build();
        departmentService.save(departmentAventyrare);
    }
}
