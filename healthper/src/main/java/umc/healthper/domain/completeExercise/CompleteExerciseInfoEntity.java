package umc.healthper.domain.completeExercise;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
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
