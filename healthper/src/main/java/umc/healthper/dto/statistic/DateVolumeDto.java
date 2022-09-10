package umc.healthper.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class
DateVolumeDto {

    /**
     * get from DB
     */

    private LocalDate date;
    private Long volume;
    private Long exerciseTime;
}
