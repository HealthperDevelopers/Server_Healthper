package umc.healthper.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import umc.healthper.Section;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.member.Member;
import umc.healthper.repository.statistic.StatisticRepositoryImpl;
import umc.healthper.service.MemberService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({RecordRepository.class, MemberService.class})
class RecordRepositoryTest {
    @Autowired
    RecordRepository repository;

    @Autowired
    MemberService memberService;

    Member testMember;
    Long recordId;

    static LocalDate testDate;

    @BeforeAll
    static void before(){
        testDate = LocalDate.now();
    }

    @BeforeEach
    void beforeEach(){
        memberService.joinMember(-1l,"testUser");
        testMember = memberService.findByKakaoKey(-1l);
        String sections = "1001001011";
        recordId = repository.add(new RecordJPA(testMember, "good", sections, testDate));
    }

    @Test
    @DisplayName("기록 저장 성공")
    void postRecord(){

        String sections = "1001001011";
        Long response = repository.add(new RecordJPA(testMember, "good", sections, testDate));

        assertThat(response).isGreaterThan(0l);
    }

    @Test
    @DisplayName("기록 조회")
    void getRecord(){
        RecordJPA findById = repository.findById(recordId);

        assertThat(findById.getSections()).isEqualTo("1001001011");
        assertThat(findById.getComment()).isEqualTo("good");
        assertThat(findById.getCreatedDay()).isEqualTo(testDate);
        assertThat(findById.getMember()).isEqualTo(testMember);
    }

    @Test
    @DisplayName("특정 날짜 조회")
    void getTheDate(){
        List<RecordJPA> recordJPAS = repository.dayExerciseInfo(testMember.getId(), testDate);

        RecordJPA target = recordJPAS.get(0);

        assertThat(recordJPAS.size()).isEqualTo(1);
        assertThat(target.getMember()).isEqualTo(testMember);
        assertThat(target.getCreatedDay()).isEqualTo(testDate);
        assertThat(target.getSections()).isEqualTo("1001001011");
    }

    @Test
    @DisplayName("달력 정보 조회")
    void getCalender(){
        LocalDate otherDate = LocalDate.of(testDate.getYear(),testDate.getMonthValue(),(testDate.getDayOfMonth()+10)%testDate.lengthOfMonth());
        Long otherId = repository.add(new RecordJPA(testMember, "bad-day", "0000001001", otherDate));

        log.info("member id: {}",testMember.getId());
        List<RecordJPA> recordJPAS = repository.calenderSource(testMember.getId(), testDate.getYear(), testDate.getMonthValue());
        RecordJPA target = recordJPAS.get(1);

        assertThat(recordJPAS.size()).isEqualTo(2);
        assertThat(target.getSections()).isEqualTo("0000001001");
        assertThat(target.getMember()).isEqualTo(testMember);
    }
}