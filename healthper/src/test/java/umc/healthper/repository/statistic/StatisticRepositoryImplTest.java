package umc.healthper.repository.statistic;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.dto.statistic.DateVolumeDto;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@RequiredArgsConstructor
class StatisticRepositoryImplTest {

    private final StatisticRepositoryImpl repository;

    @Test
    @DisplayName("모든 기록 출력")
    void getAll(){
        List<DateVolumeDto> ppp = repository.getStatisticElements(1l, "ppp");

        for (DateVolumeDto p : ppp) {
            System.out.println("element = " + p);
        }
    }
}