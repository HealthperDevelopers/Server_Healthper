package umc.healthper.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.comment.CommentStatus;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.dto.comment.CreateCommentRequestDto;
import umc.healthper.dto.comment.CreateNestedCommentRequestDto;
import umc.healthper.exception.comment.CommentAlreadyRemovedException;
import umc.healthper.exception.comment.CommentNotFoundException;
import umc.healthper.exception.comment.CommentUnauthorizedException;
import umc.healthper.exception.member.MemberNotFoundException;
import umc.healthper.exception.post.PostNotFoundException;
import umc.healthper.repository.MemberRepository;
import umc.healthper.repository.comment.CommentRepository;
import umc.healthper.repository.post.PostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록(저장)
     *
     * @param loginMemberId 댓글 작성자(로그인 사용자)의 id
     * @param dto           댓글 생성에 필요한 정보를 담은 DTO
     * @return 생성된 댓글의 id
     */
    @Transactional
    public Long saveComment(Long loginMemberId, CreateCommentRequestDto dto) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.createComment(loginMember, post, dto.getContent());

        return commentRepository.save(comment).getId();
    }

    /**
     * 대댓글 등록(저장)
     *
     * @param loginMemberId 대댓글 작성자(로그인 사용자)의 id
     * @param dto           대댓글 생성에 필요한 정보를 담은 DTO
     * @return 생성된 대댓글의 id
     */
    @Transactional
    public Long saveNestedComment(Long loginMemberId, CreateNestedCommentRequestDto dto) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(PostNotFoundException::new);
        Comment parentComment = commentRepository.findById(dto.getParentId())
                .orElseThrow(CommentNotFoundException::new);

        Comment nestedComment = Comment.createComment(loginMember, post, dto.getContent());
        parentComment.addChildComment(nestedComment);

        return commentRepository.save(nestedComment).getId();
    }

    /**
     * Comment 조회
     *
     * @param commentId 조회할 댓글의 id
     * @return 조회된 Comment 객체 return
     */
    public Comment findById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        validateRemovedComment(comment);
        return comment;
    }

    /**
     * Comment 수정
     *
     * @param commentId 수정할 댓글의 id
     * @param content 수정할 내용
     */
    @Transactional
    public void updateComment(Long loginMemberId, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        validateRemovedComment(comment);
        validateCommentAuthority(loginMemberId, comment);

        comment.update(content);
    }

    /**
     * Comment 삭제
     *
     * @param commentId 삭제할 댓글의 id
     */
    @Transactional
    public void removeComment(Long loginMemberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        validateRemovedComment(comment);
        validateCommentAuthority(loginMemberId, comment);

        commentRepository.removeComment(comment);
    }

    /**
     * 이미 삭제된 댓글인지 검증. 이미 삭제되었다면 CommentAlreadyRemovedException throw
     *
     * @param comment 검증할 댓글
     */
    private void validateRemovedComment(Comment comment) {
        if (comment.getStatus() == CommentStatus.REMOVED) {
            throw new CommentAlreadyRemovedException();
        }
    }

    /**
     * 게시글에 대한 수정/삭제 권한이 있는지 검증. 작성자에게만 write 권한이 부여된다.
     * 게시글의 수정/삭제 권한이 없는 사용자라면 PostUnauthorizedException throw
     *
     * @param memberId 수정/삭제하려는 회원 id
     * @param comment 수정하려는 댓글
     */
    private void validateCommentAuthority(Long memberId, Comment comment) {
        if (!memberId.equals(comment.getMember().getId())) {
            throw new CommentUnauthorizedException();
        }
    }
}
