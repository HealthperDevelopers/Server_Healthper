package umc.healthper.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class DateVolumeEntity {

    /**
     * exists for controller return
     */

    private LocalDate date;
    private Long volume;
}
