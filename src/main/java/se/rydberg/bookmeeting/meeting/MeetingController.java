package se.rydberg.bookmeeting.meeting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("meeting")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("")
    public String start() {
        return "meeting/meeting-start";
    }

    @GetMapping("/new")
    public String newMeeting(Model model) {
        MeetingDTO meeting = new MeetingDTO();
        model.addAttribute("meeting", meeting);
        return "meeting/meeting-edit";
    }

    @GetMapping("/edit/{id}")
    public String editMeeting(Model model, @PathVariable String id) {
        try {
            MeetingDTO meetingDTO = meetingService.findDTOBy(UUID.fromString(id));
            model.addAttribute("meeting", meetingDTO);
            return "meeting/meeting-edit";
        } catch (Exception e) {
            model.addAttribute("error_message", e.getMessage());
            return "error/general_error";
        }
    }

    @GetMapping("/detail/{id}")
    public String viewDetail(Model model, @PathVariable String id) {
        try {
            MeetingDTO meetingDto = meetingService.findDTOBy(UUID.fromString(id));
            model.addAttribute("meeting", meetingDto);
            return "meeting/meeting-detail";
        } catch (Exception e) {
            model.addAttribute("error_message", e.getMessage());
            return "error/general_error";
        }
    }

    @PostMapping("/save")
    public String saveMeeting(@Valid MeetingDTO meetingDto, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "error_message",
                    "Det saknas tyvärr lite information för att kunna spara mötestillfället.");
            model.addAttribute("meeting", meetingDto);
            return "meeting/meeting-edit";
        }
        // todo fixa så vi inte skriver över sparat data i relationer
        // alltså hämta från db och sedan uppdatera fält
        if (meetingDto.getId() != null) {
            try {
                Meeting backendMeeting = meetingService.findBy(meetingDto.getId());
                backendMeeting.setDepartment(meetingDto.getDepartment());
                backendMeeting.setDescription(meetingDto.getDescription());
                backendMeeting.setDescriptionUrl(meetingDto.getDescriptionUrl());
                backendMeeting.setEndDate(meetingDto.getEndDate());
                backendMeeting.setEndTime(meetingDto.getEndTime());
                backendMeeting.setStartDate(meetingDto.getStartDate());
                backendMeeting.setEndTime(meetingDto.getEndTime());
                backendMeeting.setPlace(meetingDto.getPlace());
                backendMeeting.setTitle(meetingDto.getTitle());
                Meeting savedMeeting = meetingService.save(backendMeeting);
                model.addAttribute("meeting", savedMeeting);
                redirectAttributes.addFlashAttribute("message", "Mötet uppdaterat");
                return "redirect:/meeting/detail/" + savedMeeting.getId();
            } catch (NotFoundInDatabaseException e) {
                model.addAttribute("error_message", "Kan inte hitta det sparade mötet att uppdatera.");
                return "error/general_error";
            }
        } else {
            MeetingDTO savedMeetingDTO = meetingService.saveDTO(meetingDto);
            model.addAttribute("meeting", savedMeetingDTO);
            redirectAttributes.addFlashAttribute("message", "Mötestillfället är sparat.");
            String id = String.valueOf(savedMeetingDTO.getId());
            return "redirect:/meeting/detail/" + id;
        }

    }
}
