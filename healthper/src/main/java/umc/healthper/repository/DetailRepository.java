package umc.healthper.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.healthper.domain.completeExercise.CompleteExerciseInfoEntity;

import javax.persistence.EntityManager;

@AllArgsConstructor
@Repository
public class DetailRepository {
    private final EntityManager em;

    public void save(CompleteExerciseInfoEntity detail){
        em.persist(detail);
    }
}
