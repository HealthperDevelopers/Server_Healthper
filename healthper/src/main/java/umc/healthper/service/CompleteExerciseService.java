package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.Section;
import umc.healthper.domain.Member;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.domain.completeExercise.CompleteExerciseInfoEntity;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.repository.CompleteExerciseRepository;

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
    public void save(List<PostExercises> req, Long memberId, Long recordId){
        Member member = memberService.findById(memberId);
        RecordJPA record = recordService.findById(recordId);
        for (PostExercises exercise : req) {
            CompleteExercise comEx = new CompleteExercise(exercise.getExerciseName(),
                    exercise.getExerciseTime(), Section.getSectionById(exercise.getSectionId()),exercise.getDetails());
            comEx.addExercise(member, record);
            repository.save(comEx);
        }
    }

    public List<GetDetails> exList(Long recordId){
        List<GetDetails> res = new ArrayList<>();
        List<CompleteExercise> details = repository.getDetails(recordService.findById(recordId).getId());
        for (CompleteExercise detail : details) {
            GetDetails ex = new GetDetails(detail.getExerciseTime(), detail.getExerciseName(), detail.getSection());
            for (CompleteExerciseInfoEntity detailInfo : detail.getDetails())
                ex.getDetails().add(detailInfo.getDetail());
            res.add(ex);
        }
        return res;
    }
}
