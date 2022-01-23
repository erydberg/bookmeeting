package se.rydberg.bookmeeting.department;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.meeting.Meeting;

import javax.persistence.*;
import java.util.*;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id",unique=true, nullable = false)
    private UUID id;
    private String name;
    private String departmentEmail;
    private String departmentEmailPassword;
    private String description;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
    @ToString.Exclude
    private Set<MeetingAttendee> attendees = new LinkedHashSet<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
    @ToString.Exclude
    @OrderColumn(name = "orderMeeting")
    private Set<Meeting> meetings = new LinkedHashSet<>();

    public void addAttendee(MeetingAttendee attendee){
        if (attendees == null) {
            attendees = new LinkedHashSet<>();
        }
        attendees.add(attendee);
        attendee.setDepartment(this);
    }

    public void removeAttendee(MeetingAttendee attendee){
        attendees.remove(attendee);
        attendee.setDepartment(null);
    }

    public void addMeeting(Meeting meeting){
        System.out.println("adding meeting");
        if (meetings == null) {
            meetings = new LinkedHashSet<>();
        }
        meetings.add(meeting);
        meeting.setDepartment(this);
    }

    public void removeMeeting(Meeting meeting){
        meetings.remove(meeting);
        meeting.setDepartment(null);
    }

    public void addMeetings(Set<Meeting> setOfMeetings){
        if (meetings != null) {
            meetings.clear();
        }
        meetings = setOfMeetings;
    }

    public String formattedDescription() {
        return description.replaceAll("(\r\n|\n)", "<br>");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Department that = (Department) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
