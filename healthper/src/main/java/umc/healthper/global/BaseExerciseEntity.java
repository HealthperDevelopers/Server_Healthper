package umc.healthper.global;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class BaseExerciseEntity {
    @NotNull
    private Long totalExerciseTime;
    @NotNull
    private Long totalVolume;

    @PostConstruct
    public void isValid(){
        if(this.getTotalVolume() == null || this.getTotalExerciseTime() == null)
            throw new IllegalArgumentException();
    }
}
