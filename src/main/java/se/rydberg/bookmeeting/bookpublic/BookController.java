package se.rydberg.bookmeeting.bookpublic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.answer.MeetingAnswerService;
import se.rydberg.bookmeeting.attendee.AttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.attendee.MeetingAttendeeDTO;
import se.rydberg.bookmeeting.configuration.ConfigurationDTO;
import se.rydberg.bookmeeting.configuration.ConfigurationService;
import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.MeetingDTO;
import se.rydberg.bookmeeting.meeting.MeetingService;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import javax.validation.Valid;

@Controller
@RequestMapping("/bookmeeting")
public class BookController {
    private final MeetingService meetingService;
    private final DepartmentService departmentService;
    private final AttendeeService attendeeService;
    private final MeetingAnswerService answerService;
    private final ConfigurationService configurationService;

    public BookController(MeetingService meetingService, DepartmentService departmentService,
                          AttendeeService attendeeService, MeetingAnswerService answerService, ConfigurationService configurationService) {
        this.meetingService = meetingService;
        this.departmentService = departmentService;
        this.attendeeService = attendeeService;
        this.answerService = answerService;
        this.configurationService = configurationService;
    }

    @GetMapping("")
    public String startBooking(Model model){
        ConfigurationDTO configuration = configurationService.loadConfiguration();
        model.addAttribute("configuration", configuration);
        AttendeeReminder reminder = new AttendeeReminder();
        model.addAttribute("reminder", reminder);
        List<DepartmentDTO> departments = departmentService.getAllDTOs();
        model.addAttribute("departments", departments);
        return "bookmeeting/book-start";
    }

    @PostMapping("/findattendee")
    public String findAttendeeForBooking(@Valid AttendeeReminder attendeeReminder, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("error_message", "Du behöver fylla i alla uppgifter innan du kan gå vidare");
            model.addAttribute("reminder", attendeeReminder);
            List<DepartmentDTO> departments = departmentService.getAllDTOs();
            model.addAttribute("departments", departments);
            return "bookmeeting/book-start";
        }
        try {
            MeetingAttendeeDTO attendee = attendeeService.findByEmailNameDepartment(attendeeReminder.getEmail(), attendeeReminder.getName(), attendeeReminder.getDepartment().getId());
            return "redirect:/bookmeeting/attendee/" + attendee.getId();
        } catch (NotFoundInDatabaseException e) {
            List<DepartmentDTO> departments = departmentService.getAllDTOs();
            model.addAttribute("departments", departments);
            model.addAttribute("reminder", attendeeReminder);
            model.addAttribute("error_message", "Kan inte hitta någon deltagare med detta namn och denna e-postadress. Kontrollera att du fyllt i rätt och kontakta din ledare om det inte fungerar.");
            return "bookmeeting/book-start";
        }
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
