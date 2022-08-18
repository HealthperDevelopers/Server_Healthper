package umc.healthper.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.global.BaseExerciseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "RECORDS")
public class RecordJPA{
    @Id
    @GeneratedValue
    @Column(name="RECORD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private BaseExerciseEntity exerciseEntity;

    @Column(length = 30)
    private String comment;
    @Size(min = 10, max = 10)
    private String sections;
    @CreatedDate
    private LocalDate createdDay;

    @OneToMany(mappedBy = "record")
    private List<CompleteExercise> comExs = new ArrayList<>();

    public void addMemberList(Member member){
        this.setMember(member);
        member.getRecords().add(this);
    }

    public RecordJPA(Member member, String comment, String sections) {
        this.member = member;
        this.comment = comment;
        this.sections = sections;
    }
}
