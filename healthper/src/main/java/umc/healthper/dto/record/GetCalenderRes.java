package umc.healthper.dto.record;

import lombok.*;
import umc.healthper.Section;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class GetCalenderRes {
    private Integer day;
    private List<Section> sections;
}
