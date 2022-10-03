package umc.healthper.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import umc.healthper.dto.statistic.DateVolumeDto;
import umc.healthper.dto.statistic.GetStatisticRes;
import umc.healthper.repository.statistic.StatisticRepository;
import umc.healthper.service.statistic.StatisticServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {StatisticRepository.class, StatisticServiceImpl.class})
class StatisticServiceTest {
    @Autowired
    StatisticServiceImpl service;

    @MockBean
    StatisticRepository repository;

    @Test
    @DisplayName("통계 정상 로직")
    void getStatistic(){
        //given
        Long userId = 10l;
        String exerciseName = "스쿼트";
        List<DateVolumeDto> dayVolumeList = makeDateVolumeList();

        Mockito.when(repository.getStatisticElements(userId,exerciseName)).thenReturn(dayVolumeList);

        GetStatisticRes response = service.getStatisticByExerciseName(userId, exerciseName);

        assertThat(response.getTotalTime()).isEqualTo(125l);
        assertThat(response.getTotalVolume()).isEqualTo(780l);
        assertThat(response.getChart().size()).isEqualTo(5);
        verify(repository).getStatisticElements(userId,exerciseName);
    }

    List<DateVolumeDto> makeDateVolumeList(){
        List<DateVolumeDto> dayVolumeList = new ArrayList<>();
        dayVolumeList.add(new DateVolumeDto(LocalDate.of(2022,8,11),100l,10l));
        dayVolumeList.add(new DateVolumeDto(LocalDate.of(2022,8,13),120l,20l));
        dayVolumeList.add(new DateVolumeDto(LocalDate.of(2022,8,15),150l,30l));
        dayVolumeList.add(new DateVolumeDto(LocalDate.of(2022,8,17),200l,45l));
        dayVolumeList.add(new DateVolumeDto(LocalDate.of(2022,8,19),210l,20l));
        return dayVolumeList;
    }
}