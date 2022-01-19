package se.rydberg.bookmeeting.check;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import se.rydberg.bookmeeting.attendee.AttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendeeDTO;
import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.MeetingService;

@Controller
@RequestMapping("/admin/checkanswers")
public class CheckAnswersController {
    private final MeetingService meetingService;
    private final AttendeeService attendeeService;
    private final DepartmentService departmentService;

    public CheckAnswersController(MeetingService meetingService, AttendeeService attendeeService, DepartmentService departmentService) {
        this.meetingService = meetingService;
        this.attendeeService = attendeeService;
        this.departmentService = departmentService;
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

        List<MeetingAttendeeDTO> attendees = attendeeService.findAllByDepartment(meeting.getDepartment().getId());
        model.addAttribute("attendees", attendees);

        return "checkanswers/checkanswers-meeting-with-answers";
    }
}
