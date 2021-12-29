package se.rydberg.bookmeeting.meeting;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import se.rydberg.bookmeeting.answer.MeetingAnswer;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
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


    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingAnswer> meetingAnswers = new ArrayList<>();

    public void addMeetingAnswer(MeetingAnswer answer) {
        System.out.println("addMeetingAnswer");
        if (meetingAnswers == null) {
            System.out.println("den är tom!");
            meetingAnswers = new ArrayList<>();
        }
        meetingAnswers.add(answer);
        answer.setMeeting(this);
    }

    public void removeMeetingAnswer(MeetingAnswer answer) {
        meetingAnswers.remove(answer);
        answer.setMeeting(null);
    }

    // @Column(name = "updated", columnDefinition="DATETIME")

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
