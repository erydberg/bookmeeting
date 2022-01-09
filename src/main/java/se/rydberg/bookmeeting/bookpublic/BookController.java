package se.rydberg.bookmeeting.bookpublic;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.answer.MeetingAnswerRepository;
import se.rydberg.bookmeeting.answer.MeetingAnswerService;
import se.rydberg.bookmeeting.attendee.AttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.MeetingService;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/bookmeeting")
public class BookController {
    private final MeetingService meetingService;
    private final DepartmentService departmentService;
    private final AttendeeService attendeeService;
    private final MeetingAnswerService answerService;

    public BookController(MeetingService meetingService, DepartmentService departmentService,
                          AttendeeService attendeeService, MeetingAnswerRepository answerRepository, MeetingAnswerService answerService) {
        this.meetingService = meetingService;
        this.departmentService = departmentService;
        this.attendeeService = attendeeService;
        this.answerService = answerService;
    }


    @GetMapping("/test")
    public String startTest(Model model){
        Department department = Department.builder()
                .name("spårarna")
                .build();
        departmentService.save(department);
        MeetingAttendee attendee = MeetingAttendee.builder()
                .name("kalle test")
                .department(department)
                .build();
        attendeeService.save(attendee);
        Meeting meeting1 = Meeting.builder()
                .startDate(LocalDate.parse("2022-01-10"))
                .title("möte 1")
                .department(department)
                .build();
        meetingService.save(meeting1);

        Meeting meeting2 = Meeting.builder()
                .startDate(LocalDate.parse("2022-02-10"))
                .title("möte 2")
                .department(department)
                .build();
        meetingService.save(meeting2);



        List<Meeting> meetings = meetingService.allMeetingsForDepartment(department.getId());
        model.addAttribute("attendee",attendee);
        model.addAttribute("department", department);
        model.addAttribute("meetings", meetings);
        return "bookmeeting/test-start";
    }



    @GetMapping("/signup/{attendeeId}/to/{meetingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String signUp(@PathVariable String attendeeId, @PathVariable String meetingId){
        try {
            MeetingAttendee attendee = attendeeService.findBy(UUID.fromString(attendeeId));
            Meeting meeting = meetingService.getWithAnswersBy(UUID.fromString(meetingId));
            System.out.println("logg - initialt");
            System.out.println("antal svar som attendee har: " + attendee.getMeetingAnswers().size());
            System.out.println("antal svar som meeting har: " + meeting.getMeetingAnswers().size());
            MeetingAnswer answer = MeetingAnswer.builder()
                    .attendee(attendee)
                    .meeting(meeting)
                    .coming(true)
                    .build();
            answerService.save(answer);
            System.out.println("sparat svar: " + answer.getId());
            System.out.println("logg - efter att ha sparat svar");
            System.out.println("antal svar som attendee har: " + attendee.getMeetingAnswers().size());
            System.out.println("antal svar som meeting har: " + meeting.getMeetingAnswers().size());
            //attendee.addMeetingAnswer(answer);
            //attendeeService.save(attendee);
            //meeting.addMeetingAnswer(answer);
            //meetingService.save(meeting);
            System.out.println("logg - efter att ha lagt till o sparat attendee o meeting");
            System.out.println("antal svar som attendee har: " + attendee.getMeetingAnswers().size());
            System.out.println("antal svar som meeting har: " + meeting.getMeetingAnswers().size());

            System.out.println("hämtar dem igen");
            Meeting meetingFromDisk = meetingService.getWithAnswersBy(UUID.fromString(meetingId));
            System.out.println("antal svar i möten från disk: " + meetingFromDisk.getMeetingAnswers().size());
            MeetingAttendee attendeeFromDisk = attendeeService.findBy(UUID.fromString(attendeeId));
            System.out.println("antal svar hos deltagaren från disk: " + attendeeFromDisk.getMeetingAnswers().size());
            return "redirect:/bookmeeting/test";
        } catch (NotFoundInDatabaseException e) {
            e.printStackTrace();
            return "redirect:/bookmeeting/test";

        }
    }
}
