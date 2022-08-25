package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.global.collectionValid.CollectionValidator;
import umc.healthper.global.Swagger;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.CompleteExerciseService;

import java.util.List;

//@Slf4j
@RestController
@RequiredArgsConstructor  
@RequestMapping("/finish")
@Tag(name = "CompleteExercise", description = "운동 상세 정보 기록 API")
public class CompleteExerciseController {

    private final CompleteExerciseService service;
    private final CollectionValidator collectionValidator;


    @Operation(summary = "운동 상세 정보 등록", description = "record post 이후 얻어낸 ID를 이용합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = String.class))) })
    @Swagger
    @PostMapping("/{recordId}")
    public String push(@Parameter(hidden = true)@Login Long userId, @PathVariable Long recordId, @Validated @RequestBody List<PostExercises> req, BindingResult bindingResult){
        collectionValidator.validate(req, bindingResult);
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
