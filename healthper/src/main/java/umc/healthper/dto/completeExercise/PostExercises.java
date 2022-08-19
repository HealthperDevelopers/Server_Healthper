package umc.healthper.dto.completeExercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostExercises {
    private String exerciseName;
    private int sectionId;
    private Long exerciseTime;
    private List<CompleteExerciseInfo> details;
}
