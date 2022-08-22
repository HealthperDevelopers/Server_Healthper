package umc.healthper.domain.completeExercise;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import umc.healthper.Section;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.RecordJPA;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Slf4j
@Getter @Setter
@NoArgsConstructor
public class CompleteExercise {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    private LocalDate createdDay;
    private String exerciseName;
    private Long exerciseTime;
    @Enumerated(EnumType.STRING)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECORD_ID")
    private RecordJPA record;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "COMPLETE_EXERCISE_ID")
    private List<CompleteExerciseInfoEntity> details = new ArrayList<>();

    public void addExercise(Member member, RecordJPA record){
        this.setMember(member);
        member.getCompleteExercises().add(this);
        this.setRecord(record);
        record.getComExs().add(this);
    }
    public CompleteExercise(String exName, Long exTime, Section section, List<CompleteExerciseInfo> exDetails){
        this.exerciseName = exName;
        this.exerciseTime = exTime;
        this.section = section;
        this.createdDay = LocalDate.now();
        for (CompleteExerciseInfo exDetail : exDetails) {
            this.details.add(new CompleteExerciseInfoEntity(exDetail));
        }
    }
}

