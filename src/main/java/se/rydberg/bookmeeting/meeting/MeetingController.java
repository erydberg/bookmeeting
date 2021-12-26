package se.rydberg.bookmeeting.meeting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("meeting")
public class MeetingController {

    @GetMapping("")
    public String start(){
        return "meeting-start";
    }

    @GetMapping("/new")
    public String newMeeting(Model model ){
        Meeting meeting = new Meeting();
        model.addAttribute("meeting", meeting);
        return "meeting-edit";
    }

    @PostMapping("/save")
    public String saveMeeting(){

        return "";

    }
}
