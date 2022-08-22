package umc.healthper.dto.completeExercise;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.healthper.Section;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;

import java.util.ArrayList;
import java.util.List;

@Data @Slf4j
@NoArgsConstructor
//@AllArgsConstructor
public class GetDetails {
    private Long exerciseTime;
    private String exerciseName;
    private Section section;
    private List<CompleteExerciseInfo> details;

    public GetDetails(Long exerciseTime, String exerciseName, Section section) {
        this.exerciseTime = exerciseTime;
        this.exerciseName = exerciseName;
        this.section = section;
        this.details = new ArrayList<>();
    }
}
