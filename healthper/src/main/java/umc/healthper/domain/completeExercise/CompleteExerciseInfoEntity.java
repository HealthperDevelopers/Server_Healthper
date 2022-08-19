package umc.healthper.domain.completeExercise;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity @Slf4j
@Table(name ="DETAILS")
@Getter @Setter
@NoArgsConstructor
public class CompleteExerciseInfoEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    private CompleteExerciseInfo detail;

    public CompleteExerciseInfoEntity(CompleteExerciseInfo detail) {
        this.detail = detail;
    }
}
