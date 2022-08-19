package umc.healthper.dto.completeExercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.Section;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;

import java.util.ArrayList;
import java.util.List;

@Data
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
