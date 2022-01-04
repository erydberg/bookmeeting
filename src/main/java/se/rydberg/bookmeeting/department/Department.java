package se.rydberg.bookmeeting.department;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import se.rydberg.bookmeeting.attendee.MeetingAttendee;
import se.rydberg.bookmeeting.meeting.Meeting;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
    @ToString.Exclude
    private List<MeetingAttendee> attendees = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
    @ToString.Exclude
    @OrderColumn(name = "orderMeeting")
    private List<Meeting> meetings = new ArrayList<>();

    public void addAttendee(MeetingAttendee attendee){
        if (attendees == null) {
            attendees = new ArrayList<>();
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
            meetings = new ArrayList<>();
        }
        meetings.add(meeting);
        meeting.setDepartment(this);
    }

    public void removeMeeting(Meeting meeting){
        meetings.remove(meeting);
        meeting.setDepartment(null);
    }

    public void addMeetings(List<Meeting> listOfMeetings){
        if (meetings != null) {
            meetings.clear();
        }
        meetings = listOfMeetings;
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
