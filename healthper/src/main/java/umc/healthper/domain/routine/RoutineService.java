package umc.healthper.domain.routine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.healthper.domain.routine.model.GetRoutineRes;
import umc.healthper.domain.routine.model.PostRoutineReq;
import umc.healthper.domain.routine.model.PostRoutineRes;
import umc.healthper.domain.routine.repository.RoutineRepository;

import java.util.List;

@Service
public class RoutineService {
    private static RoutineRepository repository;

    @Autowired
    public RoutineService(RoutineRepository repository){
        this.repository = repository;
    }

    public PostRoutineRes pushRoutine(Long userId, PostRoutineReq req){
        PostRoutineRes res = repository.save(userId, req);
        return res;
    }

    public GetRoutineRes getRoutineInfo(Long routineId){
        GetRoutineRes routine = repository.getRoutine(routineId);
        return routine;
    }

    public List<GetRoutineRes> getRoutines(Long userId){
        List<GetRoutineRes> routines = repository.getAll(userId);
        return routines;
    }
    
    public void fixRoutine(Long routineId, PostRoutineReq postRoutineReq){
        repository.changeInfo(routineId, postRoutineReq);
    }

    public void reordering(Long routineId, int target){
        int limit = repository.getRoutine(routineId).getPriori();
        if(target < limit)seqReordering(routineId,target);
        else reverseOrdering(routineId, target);
    }

    public void seqReordering(Long routineId, int target){
        Long owner = repository.getOwner(routineId);
        int limit = repository.getRoutine(routineId).getPriori();
        List<GetRoutineRes> routineRes = repository.getAll(owner);
        for (GetRoutineRes e : routineRes) {
            if(e.getPriori() >= target && e.getPriori() < limit)
                repository.changePriory(e.getId(),e.getPriori()+1);
        }
        repository.changePriory(routineId, target);
    }

    public void reverseOrdering(Long routineId, int target){
        Long owner = repository.getOwner(routineId);
        int base = repository.getRoutine(routineId).getPriori();
        List<GetRoutineRes> routineRes = repository.getAll(owner);
        for (GetRoutineRes e : routineRes) {
            if(e.getPriori() > base && e.getPriori() <= target)
                repository.changePriory(e.getId(),e.getPriori()-1);
        }
        repository.changePriory(routineId, target);
    }

    public void deleteRoutine(Long routineId){
        repository.deleteRoutine(routineId);
    }

    public Long getOwner(Long routineId){return repository.getOwner(routineId);}
}
