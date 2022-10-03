package umc.healthper.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.report.CommentReport;
import umc.healthper.domain.report.PostReport;
import umc.healthper.exception.comment.CommentNotFoundException;
import umc.healthper.exception.member.MemberNotFoundException;
import umc.healthper.exception.post.PostNotFoundException;
import umc.healthper.exception.report.CommentReportDuplicateException;
import umc.healthper.exception.report.PostReportDuplicateException;
import umc.healthper.repository.MemberRepository;
import umc.healthper.repository.comment.CommentRepository;
import umc.healthper.repository.post.PostRepository;
import umc.healthper.repository.report.CommentReportRepository;
import umc.healthper.repository.report.PostReportRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostReportRepository postReportRepository;
    private final CommentReportRepository commentReportRepository;

    /**
     * 게시글 신고
     *
     * @param loginMemberId  신고 주체 Member id
     * @param reportedPostId 신고하려는 Post id
     * @throws MemberNotFoundException      loginMemberId에 해당하는 Member가 존재하지 않는 경우
     * @throws PostNotFoundException        reportedPostId에 해당하는 Post가 존재하지 않는 경우
     * @throws PostReportDuplicateException 이미 신고한 적 있는 게시글인 경우
     */
    @Transactional
    public void reportPost(Long loginMemberId, Long reportedPostId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        Post reportedPost = postRepository.findById(reportedPostId)
                .orElseThrow(PostNotFoundException::new);

        validateDuplicatePostReport(loginMember, reportedPost);

        PostReport postReport = PostReport.createPostReport(loginMember, reportedPost);
        postReportRepository.save(postReport);
    }

    /**
     * 게시글 신고 횟수 조회
     *
     * @param postId 신고 횟수를 조회할 게시글의 id
     * @return 게시글 신고 횟수 return
     * @throws PostNotFoundException postId에 해당하는 Post가 존재하지 않는 경우
     */
    public int getPostReportedCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        return post.getReportedCount();
    }

    /**
     * 댓글 신고
     *
     * @param loginMemberId     신고 주체 Member id
     * @param reportedCommentId 신고하려는 Comment id
     * @throws MemberNotFoundException         loginMemberId에 해당하는 Member가 존재하지 않는 경우
     * @throws CommentNotFoundException        reportedPostId에 해당하는 Comment가 존재하지 않는 경우
     * @throws CommentReportDuplicateException 이미 신고한 적 있는 댓글인 경우
     */
    @Transactional
    public void reportComment(Long loginMemberId, Long reportedCommentId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        Comment reportedComment = commentRepository.findById(reportedCommentId)
                .orElseThrow(CommentNotFoundException::new);

        validateDuplicateCommentReport(loginMember, reportedComment);

        CommentReport commentReport = CommentReport.createCommentReport(loginMember, reportedComment);
        commentReportRepository.save(commentReport);
    }

    /**
     * 댓글 신고 횟수 조회
     *
     * @param commentId 신고 횟수를 조회할 댓글의 id
     * @return 댓글 신고 횟수 return
     * @throws CommentNotFoundException commentId에 해당하는 Comment가 존재하지 않는 경우
     */
    public int getCommentReportedCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        return comment.getReportedCount();
    }

    /**
     * 이미 신고한 게시글인지 검증
     *
     * @param member       신고 주체
     * @param reportedPost 신고하려는 Post
     * @throws PostReportDuplicateException 이미 신고한 게시글인 경우
     */
    private void validateDuplicatePostReport(Member member, Post reportedPost) {
        if (postReportRepository.existsByMemberAndReportedPost(member, reportedPost)) {
            throw new PostReportDuplicateException();
        }
    }

    /**
     * 이미 신고한 댓글인지 검증
     *
     * @param member          신고 주체
     * @param reportedComment 신고하려는 Comment
     * @throws CommentReportDuplicateException 이미 신고한 댓글인 경우
     */
    private void validateDuplicateCommentReport(Member member, Comment reportedComment) {
        if (commentReportRepository.existsByMemberAndReportedComment(member, reportedComment)) {
            throw new CommentReportDuplicateException();
        }
    }
}
