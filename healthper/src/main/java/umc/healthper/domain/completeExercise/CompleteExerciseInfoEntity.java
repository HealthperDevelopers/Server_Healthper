package umc.healthper.domain.completeExercise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity @Slf4j
@Table(name ="DETAILS")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CompleteExerciseInfoEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    private CompleteExerciseInfo detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPLETE_EXERCISE_ID")
    private CompleteExercise completeExercise;

    public CompleteExerciseInfoEntity(CompleteExerciseInfo exDetail, CompleteExercise completeExercise) {
        this.detail = exDetail;
        this.completeExercise = completeExercise;
    }
}
