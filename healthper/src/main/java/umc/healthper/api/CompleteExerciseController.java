package umc.healthper.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.GetDetailsRes;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.CompleteExerciseService;

import java.util.ArrayList;
import java.util.List;

//@Slf4j
@RestController
@RequiredArgsConstructor  
@RequestMapping("/finish")
public class CompleteExerciseController {
    private final CompleteExerciseService service;

    @PostMapping("/{recordId}")
    public String push(@Login Long userId, @PathVariable Long recordId, @RequestBody List<PostExercises> req){
        service.save(req, userId, recordId);
        return "ok";
    }

    @GetMapping("/{recordId}")
    public List<GetDetails> getList(@PathVariable Long recordId){
        List<GetDetails> details = service.exList(recordId);

        return details;
    }
}
