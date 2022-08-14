package umc.healthper.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.dto.record.GetRecordRes;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.RecordService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("record")
@Slf4j
@RequiredArgsConstructor
public class RecordController {

    private final RecordService service;

    @GetMapping("/calender")
    @ResponseBody
    public List<GetCalenderRes> initPage(@Login Long loginId, @RequestParam Integer year, @RequestParam Integer month){
        return service.myCalenderPage(loginId, year, month);
    }

    @GetMapping("/info")
    @ResponseBody
    public List<GetRecordRes> getDetail(@Login Long loginId, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate theDay){
        return service.theDate(loginId, theDay);
    }

    @PostMapping
    @ResponseBody
    public String pushRecord(@Login Long loginId, @RequestBody PostRecordReq req){
        service.completeToday(loginId, req);
        return "ok";
    }
}

