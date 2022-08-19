package umc.healthper.dto.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.Section;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRecordReq {
    private String comment;
    private List<Section> sections;
}
