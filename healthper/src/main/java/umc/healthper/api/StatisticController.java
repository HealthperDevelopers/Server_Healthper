package umc.healthper.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.healthper.dto.statistic.GetStatisticRes;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.statistic.StatisticService;
import umc.healthper.service.statistic.StatisticServiceImpl;


@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService service;

    @GetMapping
    GetStatisticRes getStatisticList(@Login Long userId, @RequestParam String exerciseName){
        return service.getStatisticByExerciseName(userId, exerciseName);
    }
}
