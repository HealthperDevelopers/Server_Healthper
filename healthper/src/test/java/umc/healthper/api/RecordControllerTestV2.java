package umc.healthper.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import umc.healthper.Section;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.dto.record.GetRecordRes;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.exception.record.EmptySectionException;
import umc.healthper.global.BaseExerciseEntity;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.RecordService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(RecordController.class)
//@Import({GlobalExceptionHandler.class, MessageSource.class})
public class RecordControllerTestV2 {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    //private static MockMvc record_오류;
    @MockBean
    private RecordService recordService;

    private static MockHttpSession session;
    private static Long loginId;

    @BeforeAll
    static void login() {
        loginId = 1l;
        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginId);
    }

    @AfterAll
    static void logout() {
        session.clearAttributes();
    }

    @Test
    @DisplayName("달력 정보 가져오기")
    void getCalenderInfo() throws Exception {
        List<GetCalenderRes> response = getCalenderResList();
        Integer year = 2022;
        Integer month = 9;

        given(recordService.myCalenderPage(loginId, year, month)).willReturn(
                response);

        mockMvc.perform(get("/record/calender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .session(session)
                        .param("year", year.toString())
                        .param("month", month.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].day").value(1l))
                .andDo(print());

        verify(recordService).myCalenderPage(loginId, year, month);
    }

    private List<GetCalenderRes> getCalenderResList() {
        List<GetCalenderRes> response = new ArrayList<>();
        response.add(GetCalenderRes.builder()
                .day(1).sections(Section.strToSection("0100101000"))
                .build());
        response.add(GetCalenderRes.builder()
                .day(2).sections(Section.strToSection("1111111111"))
                .build());
        return response;
    }


    @Test
    @DisplayName("기록 POST")
    void pushRecord() throws Exception {
        PostRecordReq recordReq = postReq();

        Long response = 10l;
        given(recordService.completeToday(loginId,recordReq)).willReturn(response);

        String req = objectMapper.writeValueAsString(recordReq);

        ResultActions perform = mockMvc.perform(post("/record")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8").content(req)
                .session(session));


        perform.andExpect(status().isOk())
                        .andExpect(jsonPath("$").value(10l));

        verify(recordService).completeToday(loginId, recordReq);
    }

    private PostRecordReq postReq() {
        List<Section> sections= new ArrayList<>();
        sections.add(Section.등);
        sections.add(Section.어깨);
        BaseExerciseEntity exercise = new BaseExerciseEntity(100l, 20l);
        PostRecordReq req = PostRecordReq.builder()
                .comment("good").exerciseInfo(exercise)
                .sections(sections).build();
        return req;
    }

    @Test
    @DisplayName("빈 부위 목록 POST")
    void badPushException() throws Exception {
        //given
        PostRecordReq req = PostRecordReq.builder()
                .comment("good").exerciseInfo(new BaseExerciseEntity(100l,20l))
                .sections(new ArrayList<>()).build();

        String reqs = objectMapper.writeValueAsString(req);

        given(recordService.completeToday(loginId,req)).willThrow(new EmptySectionException());

        ResultActions perform = mockMvc.perform(post("/record")
                .contentType(MediaType.APPLICATION_JSON).content(reqs)
                .characterEncoding("utf-8")
                .session(session));

        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(40));
    }

    @Test
    @DisplayName("상세 날짜 조회")
    void getDate() throws Exception{
        LocalDate request = LocalDate.now();
        List<GetRecordRes> response = getGetRecordRes();

        given(recordService.theDate(loginId,request)).willReturn(response);

        ResultActions perform = mockMvc.perform(get("/record/info")
                .param("theDay", request.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .session(session));

        perform.andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].record_id").value(5l))
                        .andExpect(jsonPath("$[0].comment").value("good"))
                        .andExpect(jsonPath("$[0].sections").isArray())
                        .andExpect(jsonPath("$[0].exerciseEntity.totalExerciseTime").value(100));

        verify(recordService).theDate(loginId,request);
    }

    private static List<GetRecordRes> getGetRecordRes() {
        List<GetRecordRes> response = new ArrayList<>();
        response.add(new GetRecordRes(5l,"good",
                Section.strToSection("1001010110"),new BaseExerciseEntity(100l, 200l)));
        return response;
    }
}
