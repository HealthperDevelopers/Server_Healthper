package umc.healthper.dto.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.Section;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRecordRes {
    private Long record_id;
    private Long total_exercise_time;
    private Long total_volume;
    private String comment;
    private List<Section> sections;
}
