package umc.healthper.repository.statistic;

import umc.healthper.dto.statistic.DateVolumeDto;

import java.util.List;

public interface StatisticRepository {
    List<DateVolumeDto> getStatisticElements(Long userId, String exerciseName);

    List<DateVolumeDto> getStatisticElementsPaging(Long userId, String exerciseName);
}
