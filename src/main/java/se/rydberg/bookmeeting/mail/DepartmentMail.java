package se.rydberg.bookmeeting.mail;

import lombok.*;
import se.rydberg.bookmeeting.department.Department;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartmentMail {
    @NotEmpty(message = "Du behöver fylla i en titel")
    private String subject;
    private String description;
    //private List<String> recipients;
    private UUID departmentId;
    private Department department;

    public String formattedDescription() {
        return description.replaceAll("(\r\n|\n)", "<br>");
    }
}
