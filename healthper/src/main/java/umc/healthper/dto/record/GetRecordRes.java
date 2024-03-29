package umc.healthper.dto.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.Section;
import umc.healthper.global.BaseExerciseEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRecordRes {
    private Long record_id;
    private String comment;
    private List<Section> sections;
    private BaseExerciseEntity exerciseEntity;
}
