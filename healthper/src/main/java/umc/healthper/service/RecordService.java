package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.Section;
import umc.healthper.domain.Member;
import umc.healthper.domain.RecordJPA;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.dto.record.GetRecordRes;
import umc.healthper.dto.record.PostRecordReq;
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

    public List<GetRecordRes> theDate(Long loginId, int year, int month, int day){
        List<RecordJPA> records = repository.dayExerciseInfo(loginId, LocalDate.of(year,month,day));

        List<GetRecordRes> res = new ArrayList<>();

        for (RecordJPA record : records) {
            Long record_id = record.getId();
            Long total_exercise_time = 100l;
            Long total_volume = 20l;
            String comment = record.getComment();
            List<Section> sections = Section.strToSection(record.getSections());
            res.add(new GetRecordRes(record_id, total_exercise_time, total_volume, comment, sections));
        }
        return res;
    }

    @Transactional
    public void completeToday(Long loginId, PostRecordReq req){
        Member member = memberService.findById(loginId);

        repository.add(member, req);
    }

}
