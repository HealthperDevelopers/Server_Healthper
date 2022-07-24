package umc.healthper.domain.routine.model;

import lombok.*;
import umc.healthper.Section;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetRoutineRes {
    private Long id;
    private String name;
    private List<Section> sections;
    private int priori;

}
