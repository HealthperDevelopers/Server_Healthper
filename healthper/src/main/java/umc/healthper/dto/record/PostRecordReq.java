package umc.healthper.dto.record;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import umc.healthper.Section;
import umc.healthper.global.BaseExerciseEntity;
import umc.healthper.global.collectionValid.CustomValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Validated
public class PostRecordReq {
    @NotBlank
    private String comment;
    @NotEmpty
    private List<Section> sections;
    @NotNull @CustomValid
    private BaseExerciseEntity exerciseInfo;

}
