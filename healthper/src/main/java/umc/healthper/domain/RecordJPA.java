package umc.healthper.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.global.BaseExerciseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "RECORDS")
public class RecordJPA {
    @Id
    @GeneratedValue
    @Column(name="RECORD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    //    private Long total_exercise_time;
//    private Long total_volume;
    @Column(length = 30)
    private String comment;
    @Size(min = 10, max = 10)
    private String sections;
    private LocalDate createdDay;

    private BaseExerciseEntity exerciseEntity;

    @OneToMany(mappedBy = "record")
    private List<CompleteExercise> comExs;

    public void addMemberList(Member member){
        this.setMember(member);
        member.getRecords().add(this);
    }

    public RecordJPA(Member member, String comment, String sections, LocalDate createdDay) {
        this.member = member;
        this.comment = comment;
        this.sections = sections;
        this.createdDay = createdDay;
    }
}
