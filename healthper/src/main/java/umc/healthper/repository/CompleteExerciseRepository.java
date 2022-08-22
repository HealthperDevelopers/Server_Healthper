package umc.healthper.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.healthper.domain.completeExercise.CompleteExercise;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
//@Slf4j
public class CompleteExerciseRepository {

    private final EntityManager em;

    public void save(CompleteExercise completeExercise){
        em.persist(completeExercise);
    }


    public List<CompleteExercise> getDetails(Long recordId){
        List<CompleteExercise> res = em.createQuery("select T from CompleteExercise T where " +
                "T.record.id = :recordId order by T.section")
                .setParameter("recordId",recordId)
                .getResultList();

        return res;
    }

}
