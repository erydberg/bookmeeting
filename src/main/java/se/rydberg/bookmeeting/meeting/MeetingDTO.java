package se.rydberg.bookmeeting.meeting;

import lombok.*;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.department.Department;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private Status status;
    private LocalDate lastBookDate;

    public String formattedDescription() {
        return description.replaceAll("(\r\n|\n)", "<br>");
    }

    public boolean isMoreThanOneDay() {
        return !startDate.equals(endDate);
    }

    public String getBestDisplayStartAndEndDayAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder timeInformation = new StringBuilder();
        timeInformation.append(startDate);
        timeInformation.append(" kl. ");
        timeInformation.append(startTime.format(dtf));
        timeInformation.append(" - ");
        if (isMoreThanOneDay()) {
            timeInformation.append(endDate);
            timeInformation.append(" kl. ");
        }
        timeInformation.append(endTime.format(dtf));

        return timeInformation.toString();
    }

    public boolean isBookable() {
        if (status == Status.INACTIVE) {
            return false;
        }
        if (lastBookDate == null && LocalDate.now().isBefore(startDate.plusDays(1))) {
            return true;
        }
        if (lastBookDate == null && LocalDate.now().isAfter(startDate)) {
            return false;
        }
        if (lastBookDate != null && LocalDate.now().isBefore(lastBookDate.plusDays(1))) {
            return true;
        }
        if (lastBookDate != null && LocalDate.now().isAfter(lastBookDate)) {
            return false;
        }
        return false;
    }
}
