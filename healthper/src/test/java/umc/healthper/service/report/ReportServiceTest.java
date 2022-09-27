package umc.healthper.service.report;

import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostType;
import umc.healthper.exception.report.CommentReportDuplicateException;
import umc.healthper.exception.report.PostReportDuplicateException;
import umc.healthper.service.MemberService;
import umc.healthper.service.comment.CommentService;
import umc.healthper.service.post.PostService;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    ReportService reportService;

    @Test(expected = PostReportDuplicateException.class)
    @DisplayName("게시글 중복 신고")
    public void reportDuplicatePost() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);
        Post post = Post.createPost(member, PostType.NORMAL, "제목", "내용");
        postService.savePost(post);

        // when
        reportService.reportPost(member.getId(), post.getId());
        reportService.reportPost(member.getId(), post.getId());

        // then
        fail("이미 신고한 게시글을 신고하려고 했으므로 예외가 발생한다.");
    }

    @Test(expected = CommentReportDuplicateException.class)
    @DisplayName("댓글 중복 신고")
    public void reportComment() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);
        Post post = Post.createPost(member, PostType.NORMAL, "제목", "내용");
        postService.savePost(post);
        Comment comment = Comment.createComment(member, post, "댓글 내용");
        commentService.saveComment(comment);

        // when
        reportService.reportComment(member.getId(), comment.getId());
        reportService.reportComment(member.getId(), comment.getId());

        // then
        fail("이미 신고한 댓글을 신고하려고 했으므로 예외가 발생한다.");
    }
}