package se.rydberg.bookmeeting.attendee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/attendee")
public class AttendeeController {

    @GetMapping("")
    public String noAttendee(){
        System.out.println("noAttendee");
        return "attendee-start";
    }

    @PostMapping("/save")
    public String saveAttendee(@Valid MeetingAttendee attendee, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){

        return "";
    }

}
