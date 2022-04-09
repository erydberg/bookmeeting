package se.rydberg.bookmeeting.mail;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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

import se.rydberg.bookmeeting.configuration.ConfigurationDTO;
import se.rydberg.bookmeeting.configuration.ConfigurationService;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

@Controller
@RequestMapping("/admin/mail")
public class MailController {
    private final DepartmentService departmentService;
    private final ConfigurationService configurationService;

    public MailController(DepartmentService departmentService, ConfigurationService configurationService) {
        this.departmentService = departmentService;
        this.configurationService = configurationService;
    }


    @GetMapping("/createto/{departmentid}")
    public String createMailToDepartment(@PathVariable String departmentid, Model model){
        try {
            DepartmentDTO department = departmentService.findDTOBy(UUID.fromString(departmentid));
            model.addAttribute("department", department);
            DepartmentMail mail = DepartmentMail.builder().departmentId(department.getId()).build();
            model.addAttribute("departmentmail", mail);
            return "mail/mail-edit";
        } catch (NotFoundInDatabaseException e) {
            model.addAttribute("error_message", "Kan inte hitta avdelningen i systemet.");
            return "error/general_error";
        }
    }

    @PostMapping("/send")
    public String sendMail(@Valid DepartmentMail mail, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("error_message", "Du behöver fylla i lite mer uppgifter");
            model.addAttribute("departmentmail", mail);
            return "mail/mail-edit";
        }
        try {
            Department department = departmentService.getDepartmentWithAttendees(mail.getDepartmentId());
            mail.setDepartment(department);
            ConfigurationDTO configuration = configurationService.loadConfiguration();
            MailService mailService = getMailService(department, configuration);
            mailService.sendDepartmentMail(mail);
            model.addAttribute("message", "Har skickat till alla deltagare");
            redirectAttributes.addFlashAttribute("message", "Har skickat mail till alla deltagare");
            return "redirect:/admin/checkanswers/bydepartment/" + department.getId();

        } catch (NotFoundInDatabaseException e) {
            model.addAttribute("error_message", "Hittar inga inställningar för e-post och lösenord");
            return "error/general_error";
        }
    }


    private MailService getMailService(Department department, ConfigurationDTO configuration) throws NotFoundInDatabaseException {
        if(isNotEmpty(department.getDepartmentEmail()) && isNotEmpty(department.getDepartmentEmailPassword()) && isNotEmpty(configuration.getDomain())){
            return new MailService(department.getDepartmentEmail(), department.getDepartmentEmailPassword(), configuration.getDomain());
        }else if(isNotEmpty(configuration.getEmail()) && isNotEmpty(configuration.getEmailkey()) && isNotEmpty(configuration.getDomain())){
            configuration = configurationService.loadConfiguration();
            return new MailService(configuration.getEmail(), configuration.getEmailkey(), configuration.getDomain());
        }else{
            throw new NotFoundInDatabaseException("Kan inte hitta konfiguration för e-post i inställningarna");
        }
    }
}
