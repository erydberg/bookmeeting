package se.rydberg.bookmeeting.attendee;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;
import se.rydberg.bookmeeting.department.Department;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "attendee")
public class MeetingAttendee {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;
    @Column(length = 100)
    private String name;
    @Column(length = 100)
    private String email;
    @Enumerated(STRING)
    private Status status = Status.ACTIVE;
    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<MeetingAnswer> meetingAnswers = new LinkedHashSet<>();

    public void addMeetingAnswer(MeetingAnswer answer) {
        if (meetingAnswers == null) {
            meetingAnswers = new LinkedHashSet<>();
        }
        meetingAnswers.add(answer);
        answer.setAttendee(this);
    }

    public void removeMeetingAnswer(MeetingAnswer answer) {
        meetingAnswers.remove(answer);
        answer.setAttendee(null);
    }
}
