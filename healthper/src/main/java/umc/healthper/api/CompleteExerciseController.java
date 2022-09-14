package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.global.collectionValid.CustomValid;
import umc.healthper.global.collectionValid.ElementValidator;
import umc.healthper.global.Swagger;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.CompleteExerciseService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor  
@RequestMapping("/finish")
@Tag(name = "CompleteExercise", description = "운동 상세 정보 기록 API")
@Validated
public class CompleteExerciseController {

    private final CompleteExerciseService service;

    @Operation(summary = "운동 상세 정보 등록", description = "record post 이후 얻어낸 ID를 이용합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = String.class))) })
    @Swagger
    @PostMapping("/{recordId}")
    public String push(@Parameter(hidden = true)@Login Long userId, @PathVariable Long recordId, @CustomValid @RequestBody List<PostExercises> req){
        for (PostExercises r : req) {
            log.info("{}", r.toString());
        }

        service.save(req, userId, recordId);
        return "ok";
    }

    @Operation(summary = "운동 상세 정보 조회", description = "이전에 얻게된 record ID를 이용합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GetDetails.class)))})
    @Swagger
    @GetMapping("/{recordId}")
    public List<GetDetails> getList(@PathVariable Long recordId){
        List<GetDetails> details = service.exList(recordId);

        return details;
    }
}
