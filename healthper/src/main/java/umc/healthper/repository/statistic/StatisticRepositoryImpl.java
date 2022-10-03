package umc.healthper.repository.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
        List<DateVolumeDto> resultList = em.createQuery("select new umc.healthper.dto.statistic.DateVolumeDto(ce.createdDay, sum(d.detail.repeatTime*d.detail.weight), sum(ce.exerciseTime)) from CompleteExercise ce\n" +
                        "  left join CompleteExerciseInfoEntity d on d.completeExercise.id = ce.id where ce.member.id =:userId and ce.exerciseName=:exerciseName group by ce.createdDay", DateVolumeDto.class)
                .setParameter("userId", userId)
                .setParameter("exerciseName", exerciseName)
                .getResultList();

        return resultList;
    }

    @Override
    public List<DateVolumeDto> getStatisticElementsPaging(Long userId, String exerciseName) {
        List<DateVolumeDto> resultList = em.createQuery("select new umc.healthper.dto.statistic.DateVolumeDto(ce.createdDay, sum(d.detail.repeatTime*d.detail.weight), sum(ce.exerciseTime)) from CompleteExercise ce\n" +
                        "  left join CompleteExerciseInfoEntity d on d.completeExercise.id = ce.id where ce.member.id =:userId and ce.exerciseName=:exerciseName group by ce.createdDay", DateVolumeDto.class)
                .setParameter("userId", userId)
                .setParameter("exerciseName", exerciseName)
                .setFirstResult(1).setMaxResults(2)
                .getResultList();

        return resultList;
    }
}
