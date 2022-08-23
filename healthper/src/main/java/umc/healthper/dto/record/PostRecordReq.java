package umc.healthper.dto.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.Section;
import umc.healthper.global.BaseExerciseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRecordReq {
    @NotNull
    private String comment;
    @NotNull
    private List<Section> sections;
    @NotNull
    private BaseExerciseEntity exerciseInfo;
}
