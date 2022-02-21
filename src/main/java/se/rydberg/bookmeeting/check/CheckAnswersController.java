package se.rydberg.bookmeeting.check;

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
import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.MeetingService;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

@Controller
@RequestMapping("/admin/checkanswers")
public class CheckAnswersController {
    private final MeetingService meetingService;
    private final AttendeeService attendeeService;
    private final DepartmentService departmentService;
    private final MeetingAnswerService answerService;

    public CheckAnswersController(MeetingService meetingService, AttendeeService attendeeService, DepartmentService departmentService, MeetingAnswerService answerService) {
        this.meetingService = meetingService;
        this.attendeeService = attendeeService;
        this.departmentService = departmentService;
        this.answerService = answerService;
    }

    @GetMapping("")
    public String start(Model model){
        List<DepartmentDTO> departments = departmentService.getAllDTOs();
        model.addAttribute("departments", departments);
        return "checkanswers/checkanswers-start";
    }

    @GetMapping("/bydepartment/{id}")
    public String byDepartment(@PathVariable String id, Model model){
        try {
            DepartmentDTO department = departmentService.findDTOBy(UUID.fromString(id));
            model.addAttribute("department", department);
            List<Meeting> meetings = meetingService.allMeetingsForDepartment(UUID.fromString(id));
            model.addAttribute("meetings", meetings);
            return "checkanswers/checkanswers-selectmeeting";
        } catch (Exception e) {
            model.addAttribute("error_message", "Kan inte hitta avdelningen i systemet.");
            return "error/general_error";
        }
    }

    @GetMapping("/bymeeting/{id}")
    public String byMeeting(@PathVariable String id, Model model){
        Meeting meeting = meetingService.getWithAnswersBy(UUID.fromString(id));
        model.addAttribute("meeting", meeting);
        List<UUID> answerIds = meeting.getMeetingAnswers().stream().map(meetingAnswer -> meetingAnswer.getAttendee().getId()).collect(Collectors.toList());
        model.addAttribute("answerids", answerIds);
        List<MeetingAttendeeDTO> attendees = attendeeService.findAllByDepartment(meeting.getDepartment().getId());
        model.addAttribute("attendees", attendees);

        return "checkanswers/checkanswers-meeting-with-answers";
    }

    @PutMapping("/attendee/{attendeeId}/attended/{meetingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveAttendedAttendee(@PathVariable String attendeeId, @PathVariable String meetingId){
        Optional<MeetingAnswer> answer = answerService.findBy(UUID.fromString(attendeeId), UUID.fromString(meetingId));
        if(answer.isPresent()){
            answer.get().setAttended(true);
            answerService.save(answer.get());
        } else {
            try {
                MeetingAttendee attendee = attendeeService.findBy(UUID.fromString(attendeeId));
                Meeting meeting = meetingService.findBy(UUID.fromString(meetingId));
                MeetingAnswer newanswer = MeetingAnswer.builder().attendee(attendee).meeting(meeting).coming(true).attended(true).build();
            answerService.save(newanswer);
            } catch (NotFoundInDatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    @PutMapping("/attendee/{attendeeId}/skipped/{meetingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveAttendeeSkippedMeeting(@PathVariable String attendeeId, @PathVariable String meetingId){
        Optional<MeetingAnswer> answer = answerService.findBy(UUID.fromString(attendeeId), UUID.fromString(meetingId));
        if(answer.isPresent()){
            answer.get().setAttended(false);
            answerService.save(answer.get());
        } else {
            try {
                MeetingAttendee attendee = attendeeService.findBy(UUID.fromString(attendeeId));
                Meeting meeting = meetingService.findBy(UUID.fromString(meetingId));
                MeetingAnswer newanswer = MeetingAnswer.builder().attendee(attendee).meeting(meeting).coming(false).attended(false).build();
                answerService.save(newanswer);
            } catch (NotFoundInDatabaseException e) {
                e.printStackTrace();
            }
        }
    }
}
