package se.rydberg.bookmeeting.meeting;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.department.Department;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "meeting")
public class Meeting {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id",unique=true, nullable = false)
    private UUID id;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String title;
    private String description;
    private String descriptionUrl;
    private String place;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    private LocalDate lastBookDate;

    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<MeetingAnswer> meetingAnswers = new LinkedHashSet<>();

    public void addMeetingAnswer(MeetingAnswer answer) {
        if (meetingAnswers == null) {
            meetingAnswers = new LinkedHashSet<>();
        }
        meetingAnswers.add(answer);
        answer.setMeeting(this);
    }

    public void removeMeetingAnswer(MeetingAnswer answer) {
        meetingAnswers.remove(answer);
        answer.setMeeting(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Meeting meeting = (Meeting) o;
        return id != null && Objects.equals(id, meeting.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
