package umc.healthper.domain.completeExercise;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;


@Embeddable
@Getter @Slf4j @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompleteExerciseInfo {

    @Min(1)
    private Long setNumber;
    @Min(1)
    private Long repeatTime;
    @Min(1)
    private Long weight;
}
