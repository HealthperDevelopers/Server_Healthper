package umc.healthper.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import umc.healthper.Section;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberStatus;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.dto.record.GetRecordRes;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.exception.completeExercise.NotMatchOwnerException;
import umc.healthper.exception.record.EmptySectionException;
import umc.healthper.exception.record.RecordNotFoundByIdException;
import umc.healthper.global.BaseExerciseEntity;
import umc.healthper.repository.RecordRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest(classes = {RecordService.class, RecordRepository.class, MemberService.class})
class RecordServiceTest{

    @Autowired
    RecordService service;

    @MockBean
    RecordRepository repository;

    @MockBean
    MemberService memberService;
    static Member testUser;
    static LocalDate testDate;
    @BeforeAll
    static void before(){
        testDate = LocalDate.now();
        testUser = Member.createMember(2022l, "test");

    }

    /**
     * 없는 운동 조회시
     */
    @Test
    @DisplayName("없는 기록 id 조회")
    void incorrectGet(){
        Long wrongRecordId = 500l;
        RecordNotFoundByIdException exception = assertThrows(RecordNotFoundByIdException.class, () -> {
            service.findById(wrongRecordId, testUser.getId());
        });

        verify(repository).findById(wrongRecordId);
    }

    @Test
    @DisplayName("id 정상 조회")
    void getRecord(){
        Long recordId = 1l;
        String comment = "good-day";
        String sections = "0100101001";
        RecordJPA response = new RecordJPA(Member.createMember(2022l, "test"), comment, sections, testDate);
        Mockito.when(repository.findById(recordId)).thenReturn(response);

        RecordJPA findRecord = service.findById(recordId, testUser.getId());


        assertThat(findRecord.getComment()).isEqualTo(comment);
        assertThat(findRecord.getSections()).isEqualTo(sections);
        assertThat(findRecord.getCreatedDay()).isEqualTo(testDate);
    }

    @Test
    @DisplayName("calender 정보 처리")
    void getCalender(){
        int year = 2022;
        int month = 8;
        int day = 10;
        String sections = "00100101110";

        List<RecordJPA> response = getRecordJPAS(year, month,day, sections);
        Mockito.when(repository.calenderSource(testUser.getId(),year,month)).thenReturn(response);

        List<GetCalenderRes> getCalenderRes = service.myCalenderPage(testUser.getId(), year, month);
        GetCalenderRes target = getCalenderRes.get(0);
        assertThat(target.getDay()).isEqualTo(day);
        assertThat(target.getSections()).isEqualTo(Section.strToSection(sections));
    }

    private static List<RecordJPA> getRecordJPAS(int year, int month, int day, String sections) {
        List<RecordJPA> response = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, day);
        response.add(new RecordJPA(testUser,"good-day",sections,date));
        return response;
    }

    @Test
    @DisplayName("달력 상세 정보 조회")
    void getDetailInfo(){
        List<RecordJPA> response = new ArrayList<>();

        String sectionStr = "01001010010";
        RecordJPA element = new RecordJPA(testUser, "good", sectionStr, testDate);
        element.setExerciseEntity(new BaseExerciseEntity(100l,200l));
        response.add(element);

        Mockito.when(repository.dayExerciseInfo(testUser.getId(),testDate)).thenReturn(response);

        List<GetRecordRes> getRecordRes = service.theDate(testUser.getId(), testDate);
        GetRecordRes target = getRecordRes.get(0);
        assertThat(target.getSections()).isEqualTo(Section.strToSection(sectionStr));
        assertThat(target.getComment()).isEqualTo("good");
        assertThat(target.getExerciseEntity()).isEqualTo(new BaseExerciseEntity(100l,200l));
    }

    @Test
    @DisplayName("달력 정보 등록 변환 검증")
    void postInfo(){
        String comment = "good";
        List<Section> sections = Section.strToSection("1001001010");
        BaseExerciseEntity exerciseInfo = new BaseExerciseEntity(100l, 200l);
        PostRecordReq request = new PostRecordReq(comment, sections, exerciseInfo);

        RecordJPA recordJPA = service.postDTOtoDomainDate(testUser, request,testDate);

        assertThat(recordJPA.getCreatedDay()).isEqualTo(LocalDate.now());
        assertThat(recordJPA.getComment()).isEqualTo(comment);
        assertThat(recordJPA.getExerciseEntity().getTotalVolume()).isEqualTo(200l);
        assertThat(recordJPA.getSections()).isEqualTo("1001001010");
    }

    @Test
    @DisplayName("부위 목록이 비어있을 경우 exception")
    void badPostInfo(){
        String comment = "good";
        List<Section> sections = Section.strToSection("0000000000");
        BaseExerciseEntity exerciseInfo = new BaseExerciseEntity(100l, 200l);
        PostRecordReq request = new PostRecordReq(comment, sections, exerciseInfo);

        EmptySectionException exception = assertThrows(EmptySectionException.class, () -> {
            service.postDTOtoDomainDate(testUser, request,testDate);
        });

        assertThat(exception).isInstanceOf(EmptySectionException.class);
    }

    @Test
    @DisplayName("Record의 주인이 요청한 사람과 다를 경우 exception")
    void badRecordId(){
        Long recordId = 100l;
        Long badUserId = 50l;
        RecordJPA response = new RecordJPA();
        response.setMember(testUser);
        Mockito.when(repository.findById(recordId)).thenReturn(response);

        NotMatchOwnerException exception = assertThrows(NotMatchOwnerException.class, () -> {
            service.findById(recordId, badUserId);
        });

        assertThat(exception).isInstanceOf(NotMatchOwnerException.class);
    }
}