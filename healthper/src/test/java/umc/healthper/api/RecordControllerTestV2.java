package umc.healthper.api;

import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import umc.healthper.Section;
import umc.healthper.dto.record.GetCalenderRes;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.RecordService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(RecordController.class)
public class RecordControllerTestV2 {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecordService recordService;

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
    @DisplayName("달력 정보 가져오기")
    void getCalenderInfo() throws Exception{
        List<GetCalenderRes> response = getCalenderResList();
        Integer year = 2022;
        Integer month = 9;

        given(recordService.myCalenderPage(loginId,year,month)).willReturn(
                response);

        mockMvc.perform(get("/record/calender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .session(session)
                        .param("year", year.toString())
                        .param("month",month.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].day").value(1l))
                .andDo(print());

        verify(recordService).myCalenderPage(loginId,year,month);
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
}
