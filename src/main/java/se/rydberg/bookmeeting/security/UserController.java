package se.rydberg.bookmeeting.security;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import java.util.UUID;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String start(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "users/users-start";
    }

    @GetMapping("/new")
    public String newuser(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user",user);
        return "users/user-edit";
    }

    @PostMapping("/save")
    public String save(@Valid UserDTO userDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("error_message", "Inte allt klart för att spara användaren. Fyll i rätt uppgifter");
            model.addAttribute("user", userDto);
            return "users/user-edit";
        }else{
            User savedUser = userService.savenew(userDto);
            redirectAttributes.addFlashAttribute("message", "Användare " + savedUser.getUsername() + " skapad.");
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable String id, Model model){
        try {
            UserDTO user = userService.findDTOby(UUID.fromString(id));
            model.addAttribute("user", user);
            return "users/user-edit";
        } catch (NotFoundInDatabaseException e) {
            model.addAttribute("error_message", "Kan inte hitta användare att redigera");
            return "error/general_error";
        }


    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id){
        userService.delete(UUID.fromString(id));
        return "redirect:/users";
    }
}
