package se.rydberg.bookmeeting;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import se.rydberg.bookmeeting.attendee.AttendeeService;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.configuration.ConfigurationDTO;
import se.rydberg.bookmeeting.configuration.ConfigurationService;
import se.rydberg.bookmeeting.department.Department;
import se.rydberg.bookmeeting.department.DepartmentService;
import se.rydberg.bookmeeting.meeting.Meeting;
import se.rydberg.bookmeeting.meeting.MeetingService;
import se.rydberg.bookmeeting.security.Role;
import se.rydberg.bookmeeting.security.RoleService;
import se.rydberg.bookmeeting.security.User;
import se.rydberg.bookmeeting.security.UserService;

@Controller
@RequestMapping("/")
public class StartController {

    private final DepartmentService departmentService;
    private final AttendeeService attendeeService;
    private final MeetingService meetingService;
    private final ConfigurationService configurationService;
    private final RoleService roleService;
    private final UserService userService;

    public StartController(DepartmentService departmentService, AttendeeService attendeeService,
                           MeetingService meetingService, ConfigurationService configurationService, RoleService roleService, UserService userService) {
        this.departmentService = departmentService;
        this.attendeeService = attendeeService;
        this.meetingService = meetingService;
        this.configurationService = configurationService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("")
    public String start(Model model) {
        ConfigurationDTO configuration = configurationService.loadConfiguration();
        model.addAttribute("configuration", configuration);
        return "start";
    }

    @GetMapping("/admin")
    public String startAdmin() {
        return "start-admin";
    }

    @GetMapping("/setup")
    public String setup(){
        if(generateFirstUser("admin", "losen")){
            return "redirect:/admin/config";
        }else{
            return "start";
        }
    }

    @GetMapping("/utv")
    public String devSetup() {
        Department department1 = Department.builder()
                .name("Spårare")
                .description("Det är bra när vi vet om du kommer! SMS till avdelningsledarna om du blir sjuk")
                .departmentEmail("spararna@scouterna.se")
                .build();
        Department department2 = Department.builder().name("Upptäckare").build();
        Department department3 = Department.builder().name("Äventyrare").build();
        Department department4 = Department.builder().name("Utmanare").build();

        departmentService.save(department1);
        departmentService.save(department2);
        departmentService.save(department3);
        departmentService.save(department4);

        System.out.println("Sparat en dev-uppsättning av avdelningar");

        MeetingAttendee attendeeSparare = MeetingAttendee.builder()
                .name("Kalle spårare")
                .email("test@mail.se")
                .emailParent1("kalleParent1@mail.se")
                .department(department1)
                .status(Status.ACTIVE)
                .build();
        MeetingAttendee attendeeSparare2 = MeetingAttendee.builder()
                .name("Stina spårare")
                .email("erik@mail.se")
                .emailParent2("stinaparent2@mail.se")
                .department(department1)
                .status(Status.ACTIVE)
                .build();
        MeetingAttendee attendeeSparare3 = MeetingAttendee.builder()
                .name("Lotta spårare")
                .email("erik@mail.se")
                .emailParent1("lottaparent1@mail.se")
                .emailParent2("lottaparent2@mail.se")
                .department(department1)
                .status(Status.ACTIVE)
                .build();
        attendeeService.save(attendeeSparare);
        attendeeService.save(attendeeSparare2);
        attendeeService.save(attendeeSparare3);

        Meeting meeting1 = Meeting.builder()
                .startDate(LocalDate.now().minusWeeks(1))
                .title("möte 1 - livlina")
                .endDate(LocalDate.now().minusWeeks(1))
                .startTime(LocalTime.NOON)
                .endTime(LocalTime.NOON.plusHours(2))
                .status(Status.ACTIVE)
                .department(department1)
                .description("Terminens första möte och vi vandrar ut i skogen och letar spårtecken i snön.")
                .build();
        meetingService.save(meeting1);

        Meeting meeting2 = Meeting.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.NOON)
                .endTime(LocalTime.NOON.plusHours(2))
                .title("möte 2 - knopar")
                .description("På detta möte kommer vi jobba med flera olika knopar")
                .descriptionUrl("http://scouterna.se")
                .status(Status.ACTIVE)
                .department(department1)
                .build();
        meetingService.save(meeting2);

        Meeting meeting3 = Meeting.builder()
                .startDate(LocalDate.now().plusWeeks(2))
                .endDate(LocalDate.now().plusMonths(2).plusDays(1))
                .startTime(LocalTime.NOON.minusHours(2))
                .endTime(LocalTime.NOON.plusHours(4))
                .lastBookDate(LocalDate.now().plusWeeks(2).minusDays(2))
                .title("Hajk")
                .description("Vi ger oss ut på vinterhajk! Se länk med packlista.")
                .status(Status.ACTIVE)
                .department(department1)
                .build();
        meetingService.save(meeting3);

        Meeting meeting4 = Meeting.builder()
                .startDate(LocalDate.now().plusWeeks(4))
                .endDate(LocalDate.now().plusWeeks(4))
                .startTime(LocalTime.NOON.minusHours(2))
                .endTime(LocalTime.NOON.plusHours(4))
                .title("möte 4 - ett långtitlat möte")
                .status(Status.ACTIVE)
                .department(department1)
                .build();
        meetingService.save(meeting4);

        Meeting meeting5 = Meeting.builder()
                .startDate(LocalDate.now().plusWeeks(5))
                .endDate(LocalDate.now().plusMonths(3))
                .startTime(LocalTime.NOON.minusHours(2))
                .endTime(LocalTime.NOON.plusHours(4))
                .title("möte 5 - ett långtitlat möte")
                .status(Status.ACTIVE)
                .department(department1)
                .build();
        meetingService.save(meeting5);

        generateFirstUser("admin", "losen");
        return "utv-start";
    }

    private boolean generateFirstUser(String userName, String password) {
        if(userService.getAllUsers().isEmpty()) {
            System.out.println("Lägger till en första admin-användare");
            Role userRole = roleService.getByName("ROLE_USER");
            if(userRole==null){
                userRole = new Role("ROLE_USER");
                roleService.save(userRole);
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String losen = "{bcrypt}" + bCryptPasswordEncoder.encode(password);

            User user = User.builder().username(userName).password(losen).enabled(true).build();
            user.addRole(userRole);
            userService.save(user);
            return true;
        } else {
            return false;
        }
    }
}