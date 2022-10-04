package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.Section;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.domain.completeExercise.CompleteExerciseInfoEntity;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.repository.CompleteExerciseRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompleteExerciseService {
    private final CompleteExerciseRepository repository;
    private final MemberService memberService;
    private final RecordService recordService;

    @Transactional
    public void save(List<PostExercises> req, Long memberId, Long recordId, LocalDate date){
        Member member = memberService.findById(memberId);
        RecordJPA record = recordService.findById(recordId, memberId);
        for (PostExercises exercise : req) {
            CompleteExercise comEx = getCompleteExercise(member, record, exercise, date);
            repository.save(comEx);
        }
    }

    public static CompleteExercise getCompleteExercise(Member member, RecordJPA record, PostExercises exercise, LocalDate date) {
        CompleteExercise comEx = new CompleteExercise(exercise.getExerciseName(),
                exercise.getExerciseTime(), Section.getSectionById(exercise.getSectionId()), exercise.getDetails(), date);
        comEx.addExercise(member, record);
        return comEx;
    }

    /**
     *
     * @param recordId
     * recordId 를 가진 상세 운동 정보 조회. (이름, 부위, 운동 시간, 운동 정보(세트 번호, 무게, 횟수)
     * @return
     */

    public List<GetDetails> exList(Long recordId, Long userId){
        List<GetDetails> res = new ArrayList<>();
        List<CompleteExercise> details = repository.getDetails(recordService.findById(recordId, userId).getId());
        for (CompleteExercise detail : details) {
            GetDetails ex = new GetDetails(detail.getExerciseTime(), detail.getExerciseName(), detail.getSection());
            for (CompleteExerciseInfoEntity detailInfo : detail.getDetails())
                ex.getDetails().add(detailInfo.getDetail());
            res.add(ex);
        }
        return res;
    }
}
