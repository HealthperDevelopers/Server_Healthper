package umc.healthper.domain.routine.model;

import lombok.*;
import umc.healthper.Section;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRoutineReq {
    private String name;
    private List<Section> sections;
}
