package se.rydberg.bookmeeting.department;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/new")
    public String newDepartment(Model model) {
        DepartmentDTO department = new DepartmentDTO();
        model.addAttribute("department", department);
        return "department/department-edit";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(Model model, @PathVariable String id){
        try{
            DepartmentDTO departmentDto = departmentService.findDTOBy(UUID.fromString(id));
            model.addAttribute("department", departmentDto);
            return "department/department-edit";
        } catch (Exception e){
            model.addAttribute("error_message", "Kunde inte hitta avdelningen att redigera");
            return "error/general_error";
        }
    }

    @GetMapping("/detail/{id}")
    public String getDepartment(Model model, @PathVariable String id){
        try {
            DepartmentDTO departmentDto = departmentService.findDTOBy(UUID.fromString(id));
            model.addAttribute("department", departmentDto);
            return "department/department-detail";
        } catch (NotFoundInDatabaseException e) {
            model.addAttribute("error_message", e.getMessage());
            return "error/general_error";
        }
    }

    @PostMapping("/save")
    public String saveDepartment(@Valid DepartmentDTO departmentDto, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            model.addAttribute("error_message", "Fyll i alla fält.");
            model.addAttribute("department", departmentDto);
            return "department/department-edit";
        }
        if(departmentDto.getId()!=null) {
            try {
                Department backendDepartment = departmentService.findBy(departmentDto.getId());
                backendDepartment.setName(departmentDto.getName());
                departmentService.save(backendDepartment);
                model.addAttribute("department", backendDepartment);
                redirectAttributes.addFlashAttribute("message", "Ändringarna sparade");
                return "redirect:/department/detail/" + backendDepartment.getId();
            } catch (NotFoundInDatabaseException e) {
                model.addAttribute("error_message", "Kan inte hitta det sparade mötet att uppdatera. Försök igen.");
                return "error/general_error";
            }
        }else{
            DepartmentDTO savedDepartment = departmentService.saveDTO(departmentDto);
            model.addAttribute("department", savedDepartment);
            redirectAttributes.addFlashAttribute("message", "Avdelning sparad");
            return "redirect:/department/detail/" + savedDepartment.getId();
        }

    }
}
