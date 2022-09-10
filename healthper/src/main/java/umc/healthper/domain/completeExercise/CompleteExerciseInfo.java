package umc.healthper.domain.completeExercise;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;


@Embeddable
@Getter @Slf4j @Setter
@EqualsAndHashCode
@AllArgsConstructor
public class CompleteExerciseInfo {

    private Long setNumber;
    private Long repeatTime;
    private Long weight;

    public CompleteExerciseInfo() {
        setNumber = 0l;
        repeatTime = 0l;
        weight = 0l;
    }
}
