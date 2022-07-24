package umc.healthper.domain.routine.repository;

import umc.healthper.domain.routine.model.GetRoutineRes;
import umc.healthper.domain.routine.model.PostRoutineReq;
import umc.healthper.domain.routine.model.PostRoutineRes;

import java.util.List;

public interface RoutineRepository {
    PostRoutineRes save(Long userId, PostRoutineReq postRoutineReq);
    GetRoutineRes getRoutine(Long routineId);
    List<GetRoutineRes> getAll(Long userId);
    void changeInfo(Long routineId, PostRoutineReq postRoutineReq);
    void changePriory(Long routineId, int target);
    void deleteRoutine(Long routineId);
    Long getOwner(Long routineId);
}
