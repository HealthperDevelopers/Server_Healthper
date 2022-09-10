package umc.healthper.service.statistic;

import umc.healthper.dto.statistic.DateVolumeDto;
import umc.healthper.dto.statistic.GetStatisticRes;

import java.util.List;

public interface StatisticService {
    GetStatisticRes getStatisticByExerciseName(Long userId, String exerciseName);
}
