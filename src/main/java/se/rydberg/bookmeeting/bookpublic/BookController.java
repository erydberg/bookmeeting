package se.rydberg.bookmeeting.bookpublic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.answer.MeetingAnswerService;
import se.rydberg.bookmeeting.attendee.AttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.attendee.MeetingAttendeeDTO;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.MeetingDTO;
import se.rydberg.bookmeeting.meeting.MeetingService;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

@Controller
@RequestMapping("/bookmeeting")
public class BookController {
    private final MeetingService meetingService;
    private final DepartmentService departmentService;
    private final AttendeeService attendeeService;
    private final MeetingAnswerService answerService;

    public BookController(MeetingService meetingService, DepartmentService departmentService,
            AttendeeService attendeeService, MeetingAnswerService answerService) {
        this.meetingService = meetingService;
        this.departmentService = departmentService;
        this.attendeeService = attendeeService;
        this.answerService = answerService;
    }

    @GetMapping("/meetinginfo/{id}")
    public String viewMeetingInfo(@PathVariable String id, Model model){
        try {
            MeetingDTO meeting = meetingService.findDTOBy(UUID.fromString(id));
            model.addAttribute("meeting", meeting);
            return "bookmeeting/meetinginfo";
        } catch (NotFoundInDatabaseException e) {
            model.addAttribute("error_message", "Kan inte hitta mer info om mötet.");
            return "bookmeeting/meetinginfo";
        }
    }

    @GetMapping("/test")
    public String startTest(Model model) {
        List<MeetingAttendee> attendees = attendeeService.findAll();
        model.addAttribute("attendees", attendees);
        return "bookmeeting/test-start";
    }

    @GetMapping("/attendee/{id}")
    public String startBookMeeting(@PathVariable String id, Model model) {
        if (id == null || id.isEmpty()) {
            model.addAttribute("error_message", "Ingen deltagare angiven. Kolla så du fick med dig hela länken.");
            return "error/general_error";
        }
        try {
            MeetingAttendeeDTO attendee = attendeeService.getDTOWithAnswers(UUID.fromString(id));
            List<Meeting> meetings = meetingService.allActiveMeetingsForDepartment(attendee.getDepartment().getId());
            List<String> acceptedMeetings = attendee.getMeetingAnswers()
                    .stream()
                    .filter(meetingAnswer -> meetingAnswer.isComing())
                    .map(meetingAnswer -> meetingAnswer.getMeeting().getId().toString())
                    .collect(Collectors.toList());
            model.addAttribute("acceptedMeetings", acceptedMeetings);
            List<String> declinedMeetings = attendee.getMeetingAnswers()
                    .stream()
                    .filter(meetingAnswer -> meetingAnswer.isNotComing())
                    .map(meetingAnswer -> meetingAnswer.getMeeting().getId().toString())
                    .collect(Collectors.toList());
            model.addAttribute("declinedMeetings", declinedMeetings);
            model.addAttribute("attendee", attendee);
            model.addAttribute("meetings", meetings);
            return "bookmeeting/book-meetings";
        } catch (NotFoundInDatabaseException e) {
            model.addAttribute("error_message", "Ingen deltagare hittad i systemet. Kolla så du fick med dig hela länken.");
            return "error/general_error";
        }
    }

    @PutMapping("/signup/{attendeeId}/to/{meetingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String signUp(@PathVariable String attendeeId, @PathVariable String meetingId) {
        // TODO behöver hantera sista anmälningsdag!
        try {
            Optional<MeetingAnswer> savedAnswer = answerService
                    .findBy(UUID.fromString(attendeeId), UUID.fromString(meetingId));
            if (savedAnswer.isPresent()) {
                savedAnswer.get().setComing(true);
                answerService.save(savedAnswer.get());
            } else {
                MeetingAttendee attendee = attendeeService.findBy(UUID.fromString(attendeeId));
                Meeting meeting = meetingService.findBy(UUID.fromString(meetingId));
                MeetingAnswer answer = MeetingAnswer.builder().attendee(attendee).meeting(meeting).coming(true).build();
                answerService.save(answer);
            }
            return "redirect:/bookmeeting/attendee/" + attendeeId;
        } catch (NotFoundInDatabaseException e) {
            return "redirect:/bookmeeting/attendee/" + attendeeId;
        }
    }

    @PutMapping("/decline/{attendeeId}/to/{meetingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String decline(@PathVariable String attendeeId, @PathVariable String meetingId) {
        // TODO behöver hantera sista anmälningsdag!
        try {
            Optional<MeetingAnswer> savedAnswer = answerService
                    .findBy(UUID.fromString(attendeeId), UUID.fromString(meetingId));
            if (savedAnswer.isPresent()) {
                System.out.println("hittar ett svar - uppdaterar");
                savedAnswer.get().setComing(false);
                answerService.save(savedAnswer.get());
            } else {
                System.out.println("skapar nytt svar");
                MeetingAttendee attendee = attendeeService.findBy(UUID.fromString(attendeeId));
                Meeting meeting = meetingService.getWithAnswersBy(UUID.fromString(meetingId));
                MeetingAnswer answer = MeetingAnswer.builder()
                        .attendee(attendee)
                        .meeting(meeting)
                        .coming(false)
                        .build();
                answerService.save(answer);
            }
            return "redirect:/bookmeeting/attendee/" + attendeeId;
        } catch (NotFoundInDatabaseException e) {
            return "redirect:/bookmeeting/attendee/" + attendeeId;
        }
    }
}
