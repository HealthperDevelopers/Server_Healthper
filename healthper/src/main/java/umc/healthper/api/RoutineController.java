package umc.healthper.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.healthper.domain.routine.RoutineService;
import umc.healthper.domain.routine.model.GetRoutineRes;
import umc.healthper.domain.routine.model.PostRoutineReq;
import umc.healthper.domain.routine.model.PostRoutineRes;

import java.util.List;

@Controller
@RestController
@RequestMapping("/routines")
public class RoutineController {
    private static RoutineService routineService;

    @Autowired
    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/{userId}")
    public PostRoutineRes makeRoutine(@PathVariable Long userId, @RequestBody PostRoutineReq req){
        PostRoutineRes res = routineService.pushRoutine(userId, req);
        return res;
    }

    @GetMapping("/routine/{routineId}")
    public GetRoutineRes getRoutineInfo(@PathVariable Long routineId){
        GetRoutineRes routineInfo = routineService.getRoutineInfo(routineId);
        return routineInfo;
    }

    @GetMapping("/{userId}")
    public List<GetRoutineRes> getRoutineList(@PathVariable Long userId){
        List<GetRoutineRes> routines = routineService.getRoutines(userId);
        return routines;
    }
    
    @PatchMapping("/routine/{routineId}")
    public String fixRoutineInfo(@PathVariable Long routineId, @RequestBody PostRoutineReq req){
        routineService.fixRoutine(routineId, req);
        return "ok";
    }

    @PostMapping("routine/priori/{routineId}/{target}")
    public String fixRoutinesOrder(@PathVariable Long routineId, @PathVariable int target){
        routineService.reordering(routineId, target);
        return "ok";
    }

    @DeleteMapping("/{routineId}")
    public String deleteRoutine(@PathVariable Long routineId){
        routineService.deleteRoutine(routineId);
        return "ok";
    }
}
