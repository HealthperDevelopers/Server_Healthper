package umc.healthper.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import umc.healthper.Section;
import umc.healthper.domain.Member;
import umc.healthper.domain.RecordJPA;
import umc.healthper.dto.record.PostRecordReq;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecordRepository{

    private final EntityManager em;

    public List<RecordJPA> calenderSource(Long loginId, int year, int month) {
        String yearStr = Integer.toString(year);
        String monthStr = Integer.toString(month);
        if(monthStr.length() == 1) monthStr = '0'+monthStr;
        //log.info("{}, {}",yearStr, monthStr);

        List<RecordJPA> resultList = em.createQuery("select RR from RecordJPA RR where RR.id in" +
                "(select min(id) from RecordJPA R WHERE R.member.id = :loginId" +
                " and DATE_FORMAT(R.createdDay,'%Y') = :year and DATE_FORMAT(R.createdDay,'%m') = :month " +
                " group by R.createdDay) order by RR.createdDay", RecordJPA.class)
                .setParameter("loginId",loginId)
                .setParameter("year", yearStr)
                .setParameter("month", monthStr)
                .getResultList();
        return resultList;
    }


    public List<RecordJPA> dayExerciseInfo(Long loginId, LocalDate theDay) {
        List<RecordJPA> resultList = em.createQuery("select R from RecordJPA R WHERE R.member.id = :loginId" +
                " and R.createdDay = :theDay order by R.id", RecordJPA.class)
                .setParameter("loginId",loginId)
                .setParameter("theDay", theDay)
                .getResultList();

        return resultList;
    }

    public Long add(RecordJPA records) {
        em.persist(records);
        return records.getId();
    }

    public RecordJPA findById(Long recordId) {
        return em.find(RecordJPA.class, recordId);
    }
}
