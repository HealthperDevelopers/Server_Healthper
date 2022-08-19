package umc.healthper.domain.completeExercise;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CompleteExerciseInfo {

    private Integer setNumber;
    private Integer repeatTime; 
    private Integer weight;
}
