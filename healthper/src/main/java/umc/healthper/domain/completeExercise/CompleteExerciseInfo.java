package umc.healthper.domain.completeExercise;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Embeddable
@Getter @Slf4j
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CompleteExerciseInfo {

    private Integer setNumber;
    private Integer repeatTime;
    private Integer weight;
}
