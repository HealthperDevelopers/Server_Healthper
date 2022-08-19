package umc.healthper.dto.completeExercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.healthper.Section;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;

import java.util.ArrayList;
import java.util.List;

@Data @Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailsRes {
    private Long exerciseTime;
    private String exerciseName;
    private Section section;
    private String info;
}
