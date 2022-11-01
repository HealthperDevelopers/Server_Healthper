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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.dto.record.GetRecordRes;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.global.Swagger;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.global.collectionValid.CustomValid;
import umc.healthper.service.RecordService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
@Tag(name = "Record", description = "기록 API")
@RestController
@RequestMapping("/record")
@Slf4j
@RequiredArgsConstructor
@Validated
public class RecordController {

    private final RecordService service;

    @Swagger
    @Operation(summary = "달력 정보", description = "년/월을 입력받아 사용자의 그 달 운동 기록을 제공합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GetCalenderRes.class)))})
    @GetMapping("/calender")
    public List<GetCalenderRes> initPage(@Parameter(hidden = true)@Login Long loginId,
                                         @RequestParam @Min(999) @Max(10000) Integer year, @RequestParam @Positive @Max(12) Integer month){
        return service.myCalenderPage(loginId, year, month);
    }
    @Swagger
    @Operation(summary = "기록 상세 정보 조회",
            description = "해당 날짜의 운동 정보[총 운동량, 총 볼륨, 운동 시간, 부위 목록] 제공" +
                    "yyyy-MM-dd 형식으로 자릿수를 맞추기 위해 한자리 수일 경우 0으로 자리 수를 맞춰야 합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GetRecordRes.class)))})
    @GetMapping("/info")
    public List<GetRecordRes> getDetail(@Parameter(hidden = true)@Login Long loginId,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate theDay){
        return service.theDate(loginId, theDay);
    }

    @Swagger
    @Operation(summary = "기록 추가", description = "전체 운동에 대한 정보 추가. 이후 리턴되는 id로 상세 운동 정보에 이용하셔야 합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Long.class)))})
    @PostMapping
    public Long pushRecord(@Parameter(hidden = true)@Login Long loginId, @CustomValid @RequestBody PostRecordReq req){
        return service.completeToday(loginId, req, LocalDate.now());
    }
}

