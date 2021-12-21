package se.rydberg.bookmeeting;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
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
    @Column(length = 50)
    private String name;

    @Column(length = 100)
    private String email;

    @OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingAnswer> meetingAnswers = new ArrayList<>();


    public void addMeetingAnswer(MeetingAnswer answer){
        if(meetingAnswers == null){
            System.out.println("meetingAnswers is empty!");
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
