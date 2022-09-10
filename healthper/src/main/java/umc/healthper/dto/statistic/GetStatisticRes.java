package umc.healthper.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStatisticRes {

    /**
     * Controller return. chart:{day, volume}, totalVolume, totalTime
     */

    private List<DateVolumeEntity> chart;
    private Long totalVolume;
    private Long totalTime;
}
