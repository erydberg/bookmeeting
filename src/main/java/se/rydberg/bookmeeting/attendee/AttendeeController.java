package se.rydberg.bookmeeting.attendee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/attendee")
public class AttendeeController {
    private final AttendeeService attendeeService;
    private final DepartmentService departmentService;

    public AttendeeController(AttendeeService attendeeService, DepartmentService departmentService) {
        this.attendeeService = attendeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("")
    public String start(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDTOs();
        model.addAttribute("departments", departments);
        return "attendee/attendee-start-admin";
    }

    @PostMapping("/save")
    public String saveAttendee(@Valid MeetingAttendee attendee, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "error_message",
                    "Det saknas tyvärr lite uppgifter eller så är något ifyllt på fel sätt");
            model.addAttribute("attendee", attendee);
            return "attendee/attendee-edit";
        }
        return "";
    }

    @GetMapping("/new")
    public String create(Model model) {
        addDepartments(model);
        MeetingAttendeeDTO attendee = new MeetingAttendeeDTO();
        model.addAttribute("attendee", attendee);
        return "attendee/attendee-edit";
    }

    @GetMapping("/edit/{id}")
    public String editAttendee(Model model, @PathVariable String id) {
        try {
            addDepartments(model);
            MeetingAttendeeDTO attendee = attendeeService.findDTOBy(UUID.fromString(id));
            model.addAttribute("attendee", attendee);
            return "attendee/attendee-edit";
        } catch (Exception e) {
            model.addAttribute("error_message", e.getMessage());
            return "error/general_error";
        }
    }

    @GetMapping("/bydepartment/{id}")
    public String byDepartment(@PathVariable String id, Model model) {
        List<MeetingAttendee> attendees = attendeeService.findAllByDepartment(UUID.fromString(id));
        model.addAttribute("attendees", attendees);
        return "attendee/attendee-bydepartment";
    }

    private void addDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDTOs();
        model.addAttribute("departments", departments);
    }
}
