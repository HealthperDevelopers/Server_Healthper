package umc.healthper.logging;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import umc.healthper.api.CommentController;
import umc.healthper.api.LoginController;
import umc.healthper.api.RecordController;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.dto.comment.CreateCommentRequestDto;
import umc.healthper.dto.record.PostRecordReq;
import umc.healthper.repository.MemberRepository;
import umc.healthper.repository.RecordRepository;
import umc.healthper.repository.post.PostLikeRepository;
import umc.healthper.repository.statistic.StatisticRepository;
import umc.healthper.service.MemberService;
import umc.healthper.service.RecordService;
import umc.healthper.service.post.PostLikeService;
import umc.healthper.service.statistic.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PointcutsTest {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    @Nested
    @DisplayName("controller aop test")
    class controllerPointcut{
        Method loginMethod;
        Method recordMethod;
        Method commentMethod;
        @BeforeEach
        public void init() throws NoSuchMethodException {
            loginMethod = LoginController.class.getMethod("login", Long.class, String.class, HttpServletRequest.class);
            recordMethod = RecordController.class.getMethod("pushRecord", Long.class, PostRecordReq.class);
            commentMethod = CommentController.class.getMethod("saveComment", CreateCommentRequestDto.class, Long.class);
        }

        @Test
        void match(){
            pointcut.setExpression("umc.healthper.logging.Pointcuts.controllerPoint()");
            assertThat(pointcut.matches(loginMethod, LoginController.class)).isTrue();
            assertThat(pointcut.matches(recordMethod, RecordController.class)).isTrue();
            assertThat(pointcut.matches(commentMethod, CommentController.class)).isTrue();
        }
    }

    @Nested
    @DisplayName("service aop test")
    class servicePointcut{
        Method memberMethod;
        Method recordMethod;
        Method statisticMethod;
        Method postLikeMethod;
        @BeforeEach
        public void init() throws NoSuchMethodException {
            memberMethod = MemberService.class.getMethod("joinMember", Long.class, String.class);
            recordMethod = RecordService.class.getMethod("myCalenderPage", Long.class, int.class, int.class);
            statisticMethod = StatisticService.class.getMethod("getStatisticByExerciseName", Long.class, String.class);
            postLikeMethod = PostLikeService.class.getMethod("addLike", Long.class, Long.class);
        }

        @Test
        void match(){
            pointcut.setExpression("umc.healthper.logging.Pointcuts.servicePoint()");
            assertThat(pointcut.matches(memberMethod, MemberService.class)).isTrue();
            assertThat(pointcut.matches(recordMethod, RecordService.class)).isTrue();
            assertThat(pointcut.matches(statisticMethod, StatisticService.class)).isTrue();
            assertThat(pointcut.matches(postLikeMethod, PostLikeService.class)).isTrue();
        }
    }

    @Nested
    @DisplayName("repository aop test")
    class repositoryPointcut{
        Method memberMethod;
        Method recordMethod;
        Method statisticMethod;
        Method postLikeMethod;
        Method proxyPostLikeMethod;
        @BeforeEach
        public void init() throws NoSuchMethodException {
            memberMethod = MemberRepository.class.getMethod("findByKakaoKey", Long.class);
            recordMethod = RecordRepository.class.getMethod("calenderSource", Long.class, int.class, int.class);
            statisticMethod = StatisticRepository.class.getMethod("getStatisticElements", Long.class, String.class);
            postLikeMethod = PostLikeRepository.class.getMethod("findByMemberAndPost", Member.class, Post.class);
            proxyPostLikeMethod = PostLikeRepository.class.getMethod("findAll", Sort.class);
        }

        @Test
        void match(){
            pointcut.setExpression("umc.healthper.logging.Pointcuts.repositoryPoint()");
            assertThat(pointcut.matches(memberMethod, MemberRepository.class)).isTrue();
            assertThat(pointcut.matches(recordMethod, RecordRepository.class)).isTrue();
            assertThat(pointcut.matches(statisticMethod, StatisticRepository.class)).isTrue();
            assertThat(pointcut.matches(postLikeMethod, PostLikeRepository.class)).isTrue();
            //spring data jpa는 proxy라서 point cut 적용 어케하지? 돌아가는 매커니즘을 알아야 함.
            assertThat(pointcut.matches(proxyPostLikeMethod, PostLikeRepository.class)).isFalse();
        }
    }
}