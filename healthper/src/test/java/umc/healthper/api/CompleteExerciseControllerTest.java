package umc.healthper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import umc.healthper.Section;
import umc.healthper.domain.completeExercise.CompleteExerciseInfo;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.dto.completeExercise.PostExercises;
import umc.healthper.global.collectionValid.CollectionValidator;
import umc.healthper.global.collectionValid.CustomValid;
import umc.healthper.global.collectionValid.ElementValidator;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.CompleteExerciseService;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CompleteExerciseController.class)
@Import({CollectionValidator.class, ElementValidator.class})
class CompleteExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompleteExerciseService service;

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
    @DisplayName("상세 운동 정보 등록. 요청 정보 검증 성공")
    void postComEx() throws Exception {
        Long recordId = 5l;
        List<PostExercises> postExercises = postReq();

        String body = objectMapper.writeValueAsString(postExercises);

        ResultActions perform = mockMvc.perform(post("/finish/" + recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8").content(body)
                .session(session));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").value("ok"));
    }

    private List<PostExercises> postReq() {
        List<PostExercises> req = new ArrayList<>();
        List<CompleteExerciseInfo> details = new ArrayList<>();
        for(Long i=1l;i<=3l;i++)
            details.add(new CompleteExerciseInfo(i,i*30,i*50));
        req.add(new PostExercises("렛풀",1,100l, details));
        return req;
    }

    @Nested
    @DisplayName("상세 운동 정보 등록 오류")
    class badPost {

        private ResultActions makePerform(String body, Long recordId) throws Exception {
            ResultActions perform = mockMvc.perform(post("/finish/" + recordId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8").content(body)
                    .session(session));
            return perform;
        }

        @Test
        @DisplayName("상세 운동 정보 등록. 요청 정보 검증 일반 필드 실패 ")
        void badPostField () throws Exception {
            Long recordId = 5L;
            List<PostExercises> postExercises = postReq();
            postExercises.get(0).setExerciseName(" ");

            String body = objectMapper.writeValueAsString(postExercises);

            ResultActions perform = makePerform(body, recordId);

            perform.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.code").value(40));
        }

        @Test
        @DisplayName("상세운동 정보 등록. json 내의 json 검증")
        void badPostDoubleJson () throws Exception {
            Long recordId = 5L;
            List<PostExercises> postExercises = postReq();
            postExercises.get(0).getDetails().get(0).setWeight(0l);

            String body = objectMapper.writeValueAsString(postExercises);

            ResultActions perform = makePerform(body, recordId);

            perform.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.code").value(40));
        }

        @Test
        @DisplayName("상세운동 정보 등록. path variable 검증")
        void postPathVerify () throws Exception {
            Long recordId = -1l;
            List<PostExercises> postExercises = postReq();

            String body = objectMapper.writeValueAsString(postExercises);

            ResultActions perform = makePerform(body, recordId);

            perform.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.code").value(40));

            perform = makePerform(body, recordId);

            perform.andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.code").value(40));
        }
    }
    @Test
    @DisplayName("상세 운동 정보 조회")
    void getComEx() throws Exception{
        Long recordId = 5L;
        List<GetDetails> response = getGetDetails();

        given(service.exList(recordId,loginId)).willReturn(response);

        ResultActions perform = mockMvc.perform(get("/finish/" + recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .session(session));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].exerciseTime").value(100l))
                .andExpect(jsonPath("$[1].exerciseName").value("렛풀"))
                .andExpect(jsonPath("$[0].section").value(Section.등.toString()));
        verify(service).exList(recordId,loginId);
    }

    @Test
    @DisplayName("없는 상세 운동 정보 조회. 빈 list 반환")
    void getComEmptyEx() throws Exception{
        Long recordId = 5L;
        List<GetDetails> response = new ArrayList<>();

        given(service.exList(recordId,loginId)).willReturn(response);

        ResultActions perform = mockMvc.perform(get("/finish/" + recordId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .session(session));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(service).exList(recordId, loginId);
    }

    private static List<GetDetails> getGetDetails() {
        List<GetDetails> response = new ArrayList<>();
        response.add(new GetDetails(100l, "데드", Section.등));
        response.add(new GetDetails(100l, "렛풀", Section.등));
        response.add(new GetDetails(100l, "풀업", Section.등));
        return response;
    }
}