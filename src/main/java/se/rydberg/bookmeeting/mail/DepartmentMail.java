package se.rydberg.bookmeeting.mail;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import lombok.*;
import se.rydberg.bookmeeting.department.Department;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartmentMail {
    @NotEmpty(message = "Du behöver fylla i en titel")
    private String subject;
    private String description;
    private UUID departmentId;
    private Department department;

    public String formattedDescription() {
        return description.replaceAll("(\r\n|\n)", "<br>");
    }
}
