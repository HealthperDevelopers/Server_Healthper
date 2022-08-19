package umc.healthper.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class BaseExerciseEntity {
    private Long totalExerciseTime;
    private Long totalVolume;
}
