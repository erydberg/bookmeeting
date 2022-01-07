package se.rydberg.bookmeeting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.rydberg.bookmeeting.attendee.AttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentDTO;
import se.rydberg.bookmeeting.department.DepartmentService;

import java.util.List;

@Controller
@RequestMapping("/")
public class StartController {

    private final DepartmentService departmentService;
    private final AttendeeService attendeeService;

    public StartController(DepartmentService departmentService, AttendeeService attendeeService) {
        this.departmentService = departmentService;
        this.attendeeService = attendeeService;
    }

    @GetMapping("")
    public String start()
    {
        return "start";
    }

    @GetMapping("/admin")
    public String startAdmin(){
        return "start-admin";
    }

    @GetMapping("/utv")
    public String devSetup(){
        Department department1 = Department.builder().name("Spårare").build();
        Department department2 = Department.builder().name("Upptäckare").build();
        Department department3 = Department.builder().name("Äventyare").build();
        Department department4 = Department.builder().name("Utmanare").build();

        departmentService.save(department1);
        departmentService.save(department2);
        departmentService.save(department3);
        departmentService.save(department4);

        System.out.println("Sparat en dev-uppsättning av avdelningar");

        MeetingAttendee attendeeSparare = MeetingAttendee.builder().name("Kalle spårare").email("test@mail.se").status(Status.ACTIVE).build();
        MeetingAttendee attendeeSparare2 = MeetingAttendee.builder().name("Stina spårare").email("erik@mail.se").status(Status.ACTIVE).build();

        attendeeService.save(attendeeSparare);
        attendeeService.save(attendeeSparare2);

        department1.addAttendee(attendeeSparare);
        department1.addAttendee(attendeeSparare2);

        departmentService.save(department1);

        return "utv-start";
    }
}
