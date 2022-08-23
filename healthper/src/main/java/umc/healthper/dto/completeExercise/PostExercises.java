package umc.healthper.dto.completeExercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data @Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PostExercises {
    @NotNull
    private String exerciseName;
    @NotNull
    private int sectionId;
    @NotNull
    private Long exerciseTime;
    @NotNull
    private List<CompleteExerciseInfo> details;
}
