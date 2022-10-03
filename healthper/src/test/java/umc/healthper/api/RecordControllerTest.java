package umc.healthper.api;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import umc.healthper.Section;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.dto.record.GetRecordRes;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.global.BaseExerciseEntity;
import umc.healthper.service.RecordService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class RecordControllerTest {

    @InjectMocks
    private RecordController recordController;

    @Mock
    private RecordService recordService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordController).build();
    }

    /**
     * 정상 로직
     */
    @Test
    @DisplayName("캘린더 조회 정상 로직")
    void initPage() throws Exception {
        //given
        Long loginId = 1l;
        Integer year = 2022;
        Integer month = 9;
        List<GetCalenderRes> response = getCalenderResList();
        lenient().doReturn(response).when(recordService)
                .myCalenderPage(eq(loginId),eq(year),eq(month));

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/record/calender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
        );

        //then
        result.andExpect(status().isOk());
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

    /**
     * 연도가 4자가 아닐 시
     */
    @Test
    @DisplayName("캘린더 조회 파라미터 오류")
    void initPageInternalError() throws Exception {
        //given
        Long loginId = 1l;
        Integer year = -1;
        Integer month = 9;
        List<GetCalenderRes> response = getCalenderResList();
        lenient().doReturn(response).when(recordService)
                .myCalenderPage(eq(loginId),eq(year),eq(month));

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/record/calender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("year", "1010101010")
                         .param("month", String.valueOf(month))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }


    /**
     * 정상 로직
     */
    @Test
    @DisplayName("날짜 조회 정상 로직")
    void getDetail() throws Exception {
        //given
        Long loginId = 1l;
        LocalDate theDay = LocalDate.now();
        List<GetRecordRes> response = getGetRecordResList();
        lenient().doReturn(response).when(recordService)
                .theDate(eq(loginId),eq(theDay));

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/record/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(new Gson().toJson(response))
                        .param("theDay", String.valueOf(theDay))
        );

        //then
        result.andExpect(status().isOk())
                //.andExpect(jsonPath("$.record_id").exists())
                .andDo(print());

    }

    /**
     * 날짜 형식 검증
     */
    @Test
    @DisplayName("날짜 조회 파라미터 검증")
    void getDetailParamValidate() throws Exception {
        //given
        Long loginId = 1l;
        LocalDate theDay = LocalDate.now();
        List<GetRecordRes> response = getGetRecordResList();
        lenient().doReturn(response).when(recordService)
                .theDate(eq(loginId),eq(theDay));

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/record/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("theDay", "2022-0900")
        );

        //then
        result.andExpect(status().is4xxClientError());

    }

    private static List<GetRecordRes> getGetRecordResList() {
        List<GetRecordRes> response = new ArrayList<>();
        response.add(new GetRecordRes(2l,"testComment1",
                Section.strToSection("0101110001"),new BaseExerciseEntity(100l, 100l)));
        response.add(new GetRecordRes(3l,"testComment2",
                Section.strToSection("1010001110"),new BaseExerciseEntity(200l, 200l)));
        return response;
    }


    /**
     * 정상 로직
     */
    @Test
    @DisplayName("기록 등록 정상 로직")
    void pushRecord() throws Exception {
        //given
        PostRecordReq postRecordReq = postRecordReq();
        Long recordId = 10l;
        lenient().doReturn(recordId).when(recordService)
                .completeToday(eq(1l), any(PostRecordReq.class));

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(postRecordReq))
        );

        //then
        result.andExpect(status().isOk());
    }

    private PostRecordReq postRecordReq(){
        List<Section> sections = Section.strToSection("1001001011");
        sections.add(Section.하체);
        return PostRecordReq.builder()
                .comment("builder-test")
                .exerciseInfo(BaseExerciseEntity.builder().totalVolume(200l).totalExerciseTime(100l).build())
                .sections(sections)
                .build();
    }

    /**
     json error 테스트
     */
    @Test
    @DisplayName("기록 등록 json 에러")
    void pushRecordJsonError() throws Exception {
        //given
        PostRecordReq postRecordReqJsonError = postRecordReqJsonError();
        Long recordId = 10l;
        lenient().doReturn(recordId).when(recordService)
                .completeToday(eq(1l), any(PostRecordReq.class));

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(postRecordReqJsonError))
        );

        //then
        ResultActions resultActions = result.andExpect(status().is4xxClientError());

    }

    private PostRecordReq postRecordReqJsonError(){
        List<Section> sections = Section.strToSection("1001001011");
        sections.add(Section.하체);
        return PostRecordReq.builder()
                //.comment("builder-test")
                .exerciseInfo(BaseExerciseEntity.builder().totalVolume(200l).totalExerciseTime(100l).build())
                .sections(sections)
                .build();
    }

    /**
     * push embeded json error
     */
    @Test
    @DisplayName("기록 등록 Embeded json 에러")
    void pushRecordEmbededJsonError() throws Exception {
        //given
        PostRecordReq postRecordReq = postRecordReqEmbeddedJsonError();
        Long recordId = 10l;
        lenient().doReturn(recordId).when(recordService)
                .completeToday(eq(1l), any(PostRecordReq.class));

        //when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(postRecordReq))
        );

        //then
        ResultActions resultActions = result.andExpect(status().is4xxClientError());
//        Assertions.assertThat(postRecordReq.getExerciseInfo()).isEqualTo(null);
       // System.out.println("postRecordReq = " + postRecordReq.getExerciseInfo());
    }

    private PostRecordReq postRecordReqEmbeddedJsonError(){
        List<Section> sections = Section.strToSection("1001001011");
        sections.add(Section.하체);
        return PostRecordReq.builder()
                .comment("builder-test")
                .exerciseInfo(BaseExerciseEntity.builder().build())
                .sections(sections)
                .build();
    }

}