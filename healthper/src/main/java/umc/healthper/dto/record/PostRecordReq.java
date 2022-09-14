package umc.healthper.dto.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.Section;
import umc.healthper.global.BaseExerciseEntity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRecordReq {
    @NotNull
    private String comment;
    @NotEmpty
    private List<Section> sections;
    @NotNull
    private BaseExerciseEntity exerciseInfo;

}
