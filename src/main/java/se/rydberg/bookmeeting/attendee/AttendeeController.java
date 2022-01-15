package se.rydberg.bookmeeting.attendee;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

@Controller
@RequestMapping("/admin/attendee")
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

    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable String id, Model model) {
        try {
            MeetingAttendeeDTO attendee = attendeeService.findDTOBy(UUID.fromString(id));
            model.addAttribute("attendee", attendee);
            return "attendee/attendee-detail";
        } catch (Exception e) {
            model.addAttribute("error_message", "Kunde inte hitta deltagaren");
            return "error/general_error";
        }
    }

    @PostMapping("/save")
    public String saveAttendee(@Valid MeetingAttendeeDTO attendee, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "error_message",
                    "Det saknas tyvärr lite uppgifter eller så är något ifyllt på fel sätt");
            addDepartments(model);
            model.addAttribute("attendee", attendee);
            return "attendee/attendee-edit";
        }
        if (attendee.getId() != null) {
            try {
                MeetingAttendee backendAttendee = attendeeService.findBy(attendee.getId());
                attendee.setMeetingAnswers(backendAttendee.getMeetingAnswers());
            } catch (NotFoundInDatabaseException e) {
            }
        }
        MeetingAttendeeDTO savedAttendee = attendeeService.saveDTO(attendee);
        redirectAttributes.addFlashAttribute("message", "Sparat " + savedAttendee.getName());

        return "redirect:/admin/attendee/detail/" + savedAttendee.getId();
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

    @GetMapping("/delete/{id}")
    public String deleteAttendee(@PathVariable String id, RedirectAttributes redirectAttributes){
        try {
            MeetingAttendee toDelete = attendeeService.findBy(UUID.fromString(id));
            UUID departmentId = null;
            if(toDelete.getDepartment()!=null){
                departmentId = toDelete.getDepartment().getId();
            }
            attendeeService.delete(toDelete);
            redirectAttributes.addFlashAttribute("message", "Deltagare borttagen");
            if(departmentId!=null){
                return "redirect:/admin/attendee/bydepartment/" + departmentId;
            }else{
                return "redirect:/admin/attendee";
            }
        } catch (NotFoundInDatabaseException e) {
            redirectAttributes.addFlashAttribute("error_message", "Det gick inte att ta bort deltagaren");
            return "error/general_error";
        }
    }

    @GetMapping("/bydepartment/{id}")
    public String byDepartment(@PathVariable String id, Model model) {
        try {
            DepartmentDTO department = departmentService.findDTOBy(UUID.fromString(id));
            model.addAttribute("department", department);
            List<MeetingAttendeeDTO> attendees = attendeeService.findAllByDepartment(UUID.fromString(id));
            model.addAttribute("attendees", attendees);
            return "attendee/attendee-bydepartment";

        } catch (Exception e) {
            model.addAttribute("error_message", "Kan inte hitta avdelningen och dess deltagare");
            return "error/general_error";
        }
    }

    private void addDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDTOs();
        model.addAttribute("departments", departments);
    }
}
