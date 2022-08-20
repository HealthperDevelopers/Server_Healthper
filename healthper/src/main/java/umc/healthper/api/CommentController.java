package umc.healthper.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.dto.comment.CreateCommentResponseDto;
import umc.healthper.dto.comment.CreateCommentRequestDto;
import umc.healthper.dto.comment.UpdateCommentRequestDto;
import umc.healthper.service.CommentService;
import umc.healthper.service.MemberService;
import umc.healthper.service.PostService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    /**
     * 댓글 등록
     */
    @PostMapping("/comment")
    public CreateCommentResponseDto saveComment(@RequestBody @Valid CreateCommentRequestDto request) {
        Member findMember = memberService.findById(request.getMemberId());
        Post findPost = postService.findPost(request.getPostId());
        Comment comment = Comment.createComment(findMember, findPost, request.getContent());

        Long parentId = request.getParentId();
        if (parentId != null) {
            Comment parentComment = commentService.findById(parentId);
            parentComment.addChildComment(comment);
        }

        Long id = commentService.saveComment(comment).getId();
        return new CreateCommentResponseDto(id);
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/comment/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestBody @Valid UpdateCommentRequestDto request) {
        commentService.updateComment(commentId, request.getContent());
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/comment/{commentId}")
    public void removeComment(@PathVariable Long commentId) {
        commentService.removeComment(commentId);
    }
}