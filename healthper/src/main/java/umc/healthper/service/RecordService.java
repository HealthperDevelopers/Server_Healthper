package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.Section;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.RecordJPA;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.dto.record.GetRecordRes;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.exception.record.RecordNotFoundByIdException;
import umc.healthper.global.BaseExerciseEntity;
import umc.healthper.repository.RecordRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final MemberService memberService;
    private final RecordRepository repository;
    public List<GetCalenderRes> myCalenderPage(Long loginId, int year, int month){
        List<RecordJPA> calenderInfos = repository.calenderSource(loginId, year, month);

        List<GetCalenderRes> res = new ArrayList<>();

        for (RecordJPA calenderInfo : calenderInfos) {
            int day = calenderInfo.getCreatedDay().getDayOfMonth();
            String sections = calenderInfo.getSections();
            List<Section> sectionList = Section.strToSection(sections);
            res.add(new GetCalenderRes(day, sectionList));
        }
        return res;
    }

    public List<GetRecordRes> theDate(Long loginId, LocalDate theDay){
        List<RecordJPA> records = repository.dayExerciseInfo(loginId, theDay);

        List<GetRecordRes> res = new ArrayList<>();

        for (RecordJPA record : records)
            res.add(transpose(record));

        return res;
    }

    private GetRecordRes transpose(RecordJPA record){
        Long record_id = record.getId();
        Long totalExerciseTime = record.getExerciseEntity().getTotalExerciseTime();
        Long totalVolume = record.getExerciseEntity().getTotalVolume();
        String comment = record.getComment();
        List<Section> sections = Section.strToSection(record.getSections());
        BaseExerciseEntity exerciseInfo = new BaseExerciseEntity(totalExerciseTime, totalVolume);

        GetRecordRes getRecordRes = new GetRecordRes(record_id, comment, sections, exerciseInfo);
        return getRecordRes;
    }

    @Transactional
    public Long completeToday(Long loginId, PostRecordReq req){
        Member member = memberService.findById(loginId);
        RecordJPA records = new RecordJPA();
        List<Section> sections = req.getSections();
        records.addMemberList(member);
        records.setComment(req.getComment());
        records.setSections(Section.listTostr(sections));
        records.setCreatedDay(LocalDate.now());
        records.setExerciseEntity(req.getExerciseInfo());

        return repository.add(records);
    }

    public RecordJPA findById(Long recordId){
        RecordJPA target = repository.findById(recordId);
        if(target == null)throw new RecordNotFoundByIdException();
        return target;
    }
}
