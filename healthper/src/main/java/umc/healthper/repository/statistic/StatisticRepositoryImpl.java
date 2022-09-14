package umc.healthper.repository.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc.healthper.dto.statistic.DateVolumeDto;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticRepositoryImpl implements StatisticRepository{
    private final EntityManager em;
    @Override
    public List<DateVolumeDto> getStatisticElements(Long userId, String exerciseName) {
        List<Object[]> resultList = em.createNativeQuery("select ce.created_day, sum(d.weight*d.repeat_time) as volume, sum(ce.exercise_time) as total_time from complete_exercise ce\n" +
                        "    left join details d on ce.id = d.complete_exercise_id where member_id =:userId and exercise_name=:exerciseName group by ce.created_day")
                .setParameter("userId", userId)
                .setParameter("exerciseName", exerciseName)
                .getResultList();
        List<DateVolumeDto> response = convertDto(resultList);
        return response;
    }

    private static List<DateVolumeDto> convertDto(List<Object[]> resultList) {
        List<DateVolumeDto> response = new ArrayList<>();
        for (Object[] objects : resultList) {
            LocalDate localDate = ((Date) objects[0]).toLocalDate();
            long vol = ((BigDecimal) objects[1]).longValue();
            long time = ((BigDecimal) objects[2]).longValue();
            response.add(new DateVolumeDto(localDate,vol,time));
        }
        return response;
    }
}
