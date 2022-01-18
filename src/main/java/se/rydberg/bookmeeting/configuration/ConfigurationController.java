package se.rydberg.bookmeeting.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/config")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }


    @GetMapping("")
    public String start(Model model){
        ConfigurationDTO configuration = configurationService.loadConfiguration();
        model.addAttribute("configuration", configuration);
        System.out.println("hämtat lösen: " + configuration.getEmailpassword());
        return "configuration/configuration-edit";
    }

    @PostMapping("/save")
    public String save(@Valid ConfigurationDTO config, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("error_message", "Det saknas lite uppgifter för att kunna spara.");
            model.addAttribute("configuration", config);
            return "configuration/configuration-edit";
        }
        configurationService.saveDTO(config);
        redirectAttributes.addFlashAttribute("message", "Konfigurationen är sparad");
        return "redirect:/admin/config";
    }
}
