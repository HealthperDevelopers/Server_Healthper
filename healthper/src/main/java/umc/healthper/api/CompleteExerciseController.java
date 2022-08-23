package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.GetDetailsRes;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.exception.ExceptionResponse;
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
    @Operation(summary = "운동 상세 정보 등록",
            description = "record post 이후 얻어낸 ID를 이용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/{recordId}")
    public String push(@Parameter(hidden = true)@Login Long userId, @PathVariable Long recordId, @RequestBody List<PostExercises> req){
        service.save(req, userId, recordId);
        return "ok";
    }

    @Operation(summary = "운동 상세 정보 조회",
            description = "이전에 얻게된 record ID를 이용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GetDetails.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @GetMapping("/{recordId}")
    public List<GetDetails> getList(@PathVariable Long recordId){
        List<GetDetails> details = service.exList(recordId);

        return details;
    }
}
