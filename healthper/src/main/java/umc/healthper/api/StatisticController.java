package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.healthper.dto.statistic.GetStatisticRes;
import umc.healthper.global.Swagger;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.statistic.StatisticService;

import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@Tag(name = "Statistic", description = "통계 API")
@Validated
public class StatisticController {
    private final StatisticService service;

    @GetMapping
    @Operation(summary = "통계 기록 조회", description = "운동 이름을 request param 형식으로 주세요. " +
            "만약 진행한 적 없는 운동에 대해서는 빈 배열로 리턴 합니다. 리턴 값은 {운동 날짜, volume값} 형태 배열, volume 총합, 운동 시간 총합")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GetStatisticRes.class))) })
    GetStatisticRes getStatisticList(@Parameter(hidden = true) @Login Long userId, @RequestParam @NotBlank String exerciseName){
        return service.getStatisticByExerciseName(userId, exerciseName);
    }
}
