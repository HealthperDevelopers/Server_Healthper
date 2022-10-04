package umc.healthper.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import umc.healthper.dto.statistic.DateVolumeEntity;
import umc.healthper.dto.statistic.GetStatisticRes;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.statistic.StatisticService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(StatisticController.class)
class StatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticService service;

    private static MockHttpSession session;
    private static Long loginId;

    @BeforeAll
    static void login(){
        loginId = 1l;
        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginId);
    }

    @After
    void logout(){
        session.clearAttributes();
    }


    @Test
    @DisplayName("통계기록 Controller")
    void getStatistic() throws Exception {
        //given
        String exerciseName = "밴치프레스";
        GetStatisticRes response = RegularGet();
        given(service.getStatisticByExerciseName(loginId, exerciseName)).willReturn(response);

        //when
        mockMvc.perform(get("/statistic")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)
                .param("exerciseName", exerciseName)
                //then
            ).andExpect(status().isOk())
                .andExpect(jsonPath("$.chart").isArray())
                .andExpect(jsonPath("$.chart[0].date").value(LocalDate.of(2022,8,20).toString()))
                .andExpect(jsonPath("$.chart[0].volume").value(100l))
                .andExpect(jsonPath("$.totalVolume").value(400l))
                .andExpect(jsonPath("$.totalTime").value(600l));

        verify(service).getStatisticByExerciseName(loginId, exerciseName);
    }

    private GetStatisticRes RegularGet() {
        GetStatisticRes response = new GetStatisticRes();
        List<DateVolumeEntity> chart = new ArrayList<>();
        chart.add(new DateVolumeEntity(LocalDate.of(2022,8,20),100l));
        chart.add(new DateVolumeEntity(LocalDate.of(2022,8,25),300l));
        return response.builder().chart(chart).totalTime(600l).totalVolume(400l).build();
    }

    /**
     * 없는 운동 조회시 빈 list 반환
     */
    @Test
    @DisplayName("없는 운동 조회시")
    void getEmptyStatistic() throws Exception {
        //given
        String exerciseName = "밴치프레스";
        GetStatisticRes response = RegularGetEmpty();
        given(service.getStatisticByExerciseName(loginId, exerciseName)).willReturn(response);

        //when
        mockMvc.perform(get("/statistic")
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                                .param("exerciseName", exerciseName)
                        //then
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.chart").isEmpty())
                .andExpect(jsonPath("$.totalVolume").value(0l))
                .andExpect(jsonPath("$.totalTime").value(0l));

        verify(service).getStatisticByExerciseName(loginId, exerciseName);
    }

    private GetStatisticRes RegularGetEmpty() {
        GetStatisticRes response = new GetStatisticRes();
        List<DateVolumeEntity> chart = new ArrayList<>();
        return response.builder().chart(chart).totalTime(0l).totalVolume(0l).build();
    }


}