package umc.healthper.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import umc.healthper.Section;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;
import umc.healthper.domain.member.Member;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.exception.completeExercise.NotMatchOwnerException;
import umc.healthper.global.collectionValid.CustomValid;
import umc.healthper.repository.CompleteExerciseRepository;
import umc.healthper.repository.RecordRepository;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;

@SpringBootTest(classes = {CompleteExerciseService.class, CompleteExerciseRepository.class
                            ,RecordService.class, MemberService.class})
class CompleteExerciseServiceTest {
    @Autowired
    CompleteExerciseService service;

    @MockBean
    CompleteExerciseRepository repository;
    @MockBean
    RecordService recordService;

    @MockBean
    MemberService memberService;

    static Member testMember;

    static Long recordId = 10l;

    @BeforeAll
    static void beforeAll(){
        testMember = Member.createMember(2002l, "test");
    }

    /**
     * recordJPA setter 설정 해야함
     * 해결법.. 찾아보자..
     */

    @Test
    @DisplayName("상세 정보 조회 성공")
    void getComEx(){
        RecordJPA mockFindById = new RecordJPA();
        mockFindById.setId(recordId);
        Mockito.when(recordService.findById(recordId,testMember.getId())).thenReturn(mockFindById);

        List<CompleteExercise> response = exResponse();

        Mockito.when(repository.getDetails(recordId)).thenReturn(response);

        List<GetDetails> getDetails = service.exList(recordId, testMember.getId());
        GetDetails target1 = getDetails.get(0);
        GetDetails target2 = getDetails.get(1);
        assertThat(getDetails.size()).isEqualTo(2);
        assertThat(target1.getExerciseName()).isEqualTo("스쿼트");
        assertThat(target1.getExerciseTime()).isEqualTo(100L);
        assertThat(target1.getSection()).isEqualTo(Section.하체);
        assertThat(target1.getDetails().size()).isEqualTo(3);

        assertThat(target2.getExerciseName()).isEqualTo("힙 어덕션");
        assertThat(target2.getExerciseTime()).isEqualTo(200L);
        assertThat(target2.getSection()).isEqualTo(Section.엉덩이);
        assertThat(target2.getDetails().size()).isEqualTo(3);
    }

    private List<CompleteExercise> exResponse() {
        List<CompleteExercise> response = new ArrayList<>();
        List<CompleteExerciseInfo> exDetails = makeDetails();
        LocalDate today = LocalDate.now();
        response.add(new CompleteExercise("스쿼트",100L, Section.하체,exDetails, today));
        List<CompleteExerciseInfo> exDetails2 = new ArrayList<>();
        exDetails2.add(new CompleteExerciseInfo(1l,5l,60l));
        exDetails2.add(new CompleteExerciseInfo(2l,5l,60l));
        exDetails2.add(new CompleteExerciseInfo(3l,5l,60l));
        response.add(new CompleteExercise("힙 어덕션",200l, Section.엉덩이, exDetails2, today));

        return response;
    }

    private List<CompleteExerciseInfo> makeDetails(){
        List<CompleteExerciseInfo> exDetails = new ArrayList<>();
        exDetails.add(new CompleteExerciseInfo(1l,5l,60l));
        exDetails.add(new CompleteExerciseInfo(2l,5l,60l));
        exDetails.add(new CompleteExerciseInfo(3l,5l,60l));
        return exDetails;
    }

}