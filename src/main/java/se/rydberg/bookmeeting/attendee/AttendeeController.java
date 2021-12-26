package se.rydberg.bookmeeting.attendee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/attendee")
public class AttendeeController {

    @GetMapping("")
    public String noAttendee(){
        System.out.println("noAttendee");
        return "attendee-start";
    }

}
