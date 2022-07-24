package umc.healthper.domain.routine;

import lombok.*;
import umc.healthper.Section;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Routine {
    private Long id;
    private Long userId;
    private String name;
    private List<Section> sections;
    private int priori;

}
