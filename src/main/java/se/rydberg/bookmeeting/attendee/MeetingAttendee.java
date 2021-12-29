package se.rydberg.bookmeeting.attendee;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import se.rydberg.bookmeeting.Status;
import se.rydberg.bookmeeting.answer.MeetingAnswer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "id",unique=true, nullable = false)
    private UUID id;
    @Column(length = 100)
    private String name;
    @Column(length = 100)
    private String email;
    @Enumerated(STRING)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingAnswer> meetingAnswers = new ArrayList<>();


    public void addMeetingAnswer(MeetingAnswer answer){
        if(meetingAnswers == null){
            meetingAnswers = new ArrayList<>();
        }
        meetingAnswers.add(answer);
        answer.setAttendee(this);
    }

    public void removeMeetingAnswer(MeetingAnswer answer){
        meetingAnswers.remove(answer);
        answer.setAttendee(null);
    }
}
