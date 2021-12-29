package se.rydberg.bookmeeting.department;

import se.rydberg.bookmeeting.meeting.Meeting;

import java.util.List;
import java.util.UUID;

public class DepartmentDTO {
    private UUID id;
    private String name;
    private List<Meeting> meetings;
}
