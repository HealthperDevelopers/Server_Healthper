package umc.healthper.service.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.dto.statistic.DateVolumeDto;
import umc.healthper.dto.statistic.DateVolumeEntity;
import umc.healthper.dto.statistic.GetStatisticRes;
import umc.healthper.repository.statistic.StatisticRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{

    private final StatisticRepository repository;
    @Override
    public GetStatisticRes getStatisticByExerciseName(Long userId, String exerciseName){
        List<DateVolumeDto> statisticElements = repository.getStatisticElements(userId, exerciseName);

        GetStatisticRes response = fillUp(statisticElements);
        return response;
    }

    private static GetStatisticRes fillUp(List<DateVolumeDto> statisticElements) {
        GetStatisticRes response = new GetStatisticRes();
        Long totalVolume = 0l;
        Long totalTime = 0l;
        response.setChart(new ArrayList<>());
        for (DateVolumeDto element : statisticElements) {
            totalVolume += element.getVolume();
            totalTime += element.getExerciseTime();
            response.getChart().add(new DateVolumeEntity(element.getDate(), element.getVolume()));
        }
        response.setTotalTime(totalTime);
        response.setTotalVolume(totalVolume);
        return response;
    }
}
