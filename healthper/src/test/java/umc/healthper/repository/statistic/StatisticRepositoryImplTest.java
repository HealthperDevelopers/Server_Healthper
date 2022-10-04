package umc.healthper.repository.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import umc.healthper.Section;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;
import umc.healthper.domain.member.Member;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.dto.statistic.DateVolumeDto;
import umc.healthper.global.BaseExerciseEntity;
import umc.healthper.global.collectionValid.CustomValid;
import umc.healthper.repository.CompleteExerciseRepository;
import umc.healthper.repository.RecordRepository;
import umc.healthper.service.CompleteExerciseService;
import umc.healthper.service.MemberService;
import umc.healthper.service.RecordService;
import umc.healthper.service.statistic.StatisticServiceImpl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({StatisticRepositoryImpl.class, RecordService.class, CompleteExerciseService.class, MemberService.class, RecordRepository.class, CompleteExerciseRepository.class})
class StatisticRepositoryImplTest {

    @Autowired
    StatisticRepository repository;

    @Autowired
    MemberService memberService;

    @Autowired
    RecordService recordService;

    @Autowired
    CompleteExerciseService completeExerciseService;

    static String testExerciseName;
    Member testMember;

    @BeforeAll
    static void beforeAll(){
        testExerciseName = "풀업";
    }

    @BeforeEach
    void beforeEach(){
        memberService.joinMember(-1l, "testUser");
        testMember = memberService.findByKakaoKey(2022l);
        //첫날
        LocalDate firstDate = LocalDate.of(2022, 10, 8);
        makeElement(firstDate);

        //둘째날
        //LocalDate secondDate = LocalDate.of(2022, 10, 10);
       // makeElement(secondDate);

        //셋째날
        //LocalDate thirdDate = LocalDate.of(2022, 10, 11);
        //makeElement(thirdDate);

    }

    private void makeElement(LocalDate theDate) {
        RecordJPA testRecord;
        PostRecordReq request = new PostRecordReq("good",Section.strToSection("1000000100"),new BaseExerciseEntity(500l, 30000l));
        Long recordId = recordService.completeToday(testMember.getId(),request,theDate);
        testRecord = recordService.findById(recordId, testMember.getId());
        log.info("testRecord: {}", testRecord.getId());

        List<PostExercises> req = new ArrayList<>();
        List<CompleteExerciseInfo> details = getDetails();

        Long volume = 0l;
        for (CompleteExerciseInfo detail : details) {
            volume += detail.getRepeatTime()*detail.getWeight();
        }
        log.info("detail volume: {}, {}", details, volume);
        req.add(new PostExercises(testExerciseName, Section.등.getId(), 140l, details));

        completeExerciseService.save(req, testMember.getId(), testRecord.getId(), theDate);
    }

    private List<CompleteExerciseInfo> getDetails() {
        List<CompleteExerciseInfo> details = new ArrayList<>();
        details.add(new CompleteExerciseInfo(1l,10l,30l));
        details.add(new CompleteExerciseInfo(2l,10l,30l));
        details.add(new CompleteExerciseInfo(3l,10l,30l));
        return details;
    }


    @Test
    @DisplayName("통계 쿼리 실행")
    void getStatic(){
        List<DateVolumeDto> statisticElements = repository.getStatisticElements(testMember.getId(), testExerciseName);
        for (DateVolumeDto statisticElement : statisticElements) {
            log.info("statisticElement = {}", statisticElement);
        }
    }
}