package se.rydberg.bookmeeting.mail;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/mail")
public class MailController {
    private final DepartmentService departmentService;

    public MailController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @GetMapping("/createto/{departmentid}")
    public String createMailToDepartment(@PathVariable String id, Model model){
        try {
            DepartmentDTO department = departmentService.findDTOBy(UUID.fromString(id));
            model.addAttribute("department", department);


        } catch (NotFoundInDatabaseException e) {
            model.addAttribute("error_message", "Kan inte hitta avdelningen i systemet.");
            return "error/general_error";
        }
        return "mail/mail-edit";
    }
}
