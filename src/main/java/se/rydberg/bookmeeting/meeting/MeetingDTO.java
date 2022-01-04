package se.rydberg.bookmeeting.meeting;

import lombok.*;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.department.Department;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDTO {
    private UUID id;
    @NotNull(message = "Du behöver ange en dag då mötet startar")
    private LocalDate startDate;
    @NotNull(message = "Du behöver ange en tid då mötet startar")
    private LocalTime startTime;
    @NotNull(message = "Du behöver ange en dag då mötet slutar")
    private LocalDate endDate;
    @NotNull(message = "Du behöver ange en tid då mötet slutar")
    private LocalTime endTime;
    @NotNull(message = "Du behöver ange ett namn på mötet")
    private String title;
    private String description;
    private String descriptionUrl;
    private String place;
    private Set<MeetingAnswer> meetingAnswers;
    private Department department;

    public String formattedDescription(){
        return description.replaceAll("(\r\n|\n)", "<br>");
    }
}
