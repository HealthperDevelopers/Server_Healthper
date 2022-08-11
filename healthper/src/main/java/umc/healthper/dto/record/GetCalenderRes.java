package umc.healthper.dto.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.Section;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCalenderRes {
    private Integer day;
    private List<Section> sections;
}
