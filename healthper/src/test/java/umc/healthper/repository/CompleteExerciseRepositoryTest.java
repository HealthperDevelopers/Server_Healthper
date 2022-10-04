package umc.healthper.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import umc.healthper.Section;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;
import umc.healthper.domain.member.Member;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.global.BaseExerciseEntity;
import umc.healthper.service.MemberService;
import umc.healthper.service.RecordService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@Import({CompleteExerciseRepository.class, MemberService.class, RecordService.class, RecordRepository.class})
class CompleteExerciseRepositoryTest {
    @Autowired
    CompleteExerciseRepository repository;

    @Autowired
    MemberService memberService;
    @Autowired
    RecordService recordService;
    Member testMember;
    RecordJPA testRecord;

    LocalDate testDate;

    @BeforeEach
    void beforeEach(){
        testDate = LocalDate.now();
        Long kakaoId = -1l;
        memberService.joinMember(kakaoId,"testUser");
        testMember = memberService.findByKakaoKey(kakaoId);

        PostRecordReq req = new PostRecordReq("test-day", Section.strToSection("1000100000"),new BaseExerciseEntity(200l,300l));
        Long recordId = recordService.completeToday(testMember.getId(), req, testDate);
        testRecord = recordService.findById(recordId, testMember.getId());
    }

    @Test
    @DisplayName("상세 운동 정보 등록")
    void postComEx(){
        List<CompleteExerciseInfo> exDetails = getExDetails();
        CompleteExercise request = new CompleteExercise("풀업",100l,Section.등,exDetails, testDate);
        request.addExercise(testMember, testRecord);
        repository.save(request);
    }

    private List<CompleteExerciseInfo> getExDetails() {
        List<CompleteExerciseInfo> exDetails = new ArrayList<>();
        exDetails.add(new CompleteExerciseInfo(1l,10l,10l));
        exDetails.add(new CompleteExerciseInfo(2l,10l,10l));
        exDetails.add(new CompleteExerciseInfo(3l,10l,10l));
        return exDetails;
    }

    @Test
    @DisplayName("상세 운동 정보 조회")
    void getComEx(){
        setUpDB();

        List<CompleteExercise> details = repository.getDetails(testRecord.getId());
        CompleteExercise target = details.get(0);

        assertThat(details.size()).isEqualTo(2);
        assertThat(target.getCreatedDay()).isEqualTo(LocalDate.now());
        assertThat(target.getExerciseName()).isEqualTo("풀업");
        assertThat(target.getSection()).isEqualTo(Section.등);
        assertThat(target.getRecord()).isEqualTo(testRecord);
        assertThat(target.getMember()).isEqualTo(testMember);
        assertThat(target.getDetails().size()).isEqualTo(3);
    }

    private void setUpDB() {
        List<CompleteExerciseInfo> exDetails = getExDetails();
        CompleteExercise request = new CompleteExercise("풀업",100l,Section.등,exDetails, testDate);
        List<CompleteExerciseInfo> exDetails2 = getExDetails();
        CompleteExercise request2 = new CompleteExercise("데드",100l,Section.등,exDetails2, testDate);
        request.addExercise(testMember, testRecord);
        request2.addExercise(testMember, testRecord);

        repository.save(request);
        repository.save(request2);
    }
}