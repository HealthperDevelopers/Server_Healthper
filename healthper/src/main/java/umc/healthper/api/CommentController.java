package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.dto.comment.*;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.comment.CommentLikeService;
import umc.healthper.service.comment.CommentService;
import umc.healthper.service.MemberService;
import umc.healthper.service.post.PostService;

import javax.validation.Valid;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;


    @Operation(summary = "댓글 생성",
            description = "댓글 정보를 받아 새로운 댓글을 생성합니다.")
    @PostMapping("/comment")
    public void saveComment(@RequestBody @Valid CreateCommentRequestDto request,
                            @Parameter(hidden = true) @Login Long loginMemberId) {
        Member member = memberService.findById(loginMemberId);
        Post post = postService.findPost(request.getPostId(), false);
        Comment comment = Comment.createComment(member, post, request.getContent());
        commentService.saveComment(comment);
    }

    @Operation(summary = "대댓글 생성",
            description = "대댓글 정보를 받아 새로운 대댓글을 생성합니다.")
    @PostMapping("/comment-nested")
    public void saveComment(@RequestBody @Valid CreateNestedCommentRequestDto requestDto,
                            @Parameter(hidden = true) @Login Long loginMemberId) {
        Member findMember = memberService.findById(loginMemberId);
        Post findPost = postService.findPost(requestDto.getPostId(), false);

        Comment comment = Comment.createComment(findMember, findPost, requestDto.getContent());
        Comment parentComment = commentService.findById(requestDto.getParentId());
        parentComment.addChildComment(comment);
        commentService.saveComment(comment);
    }

    @Operation(summary = "댓글 조회",
            description = "`commentId`에 해당하는 댓글을 조회합니다.\n\n" +
                    "**Response**\n\n" +
                    "- `status`: `NORMAL`, `REMOVED`(삭제된 댓글), `BLOCKED`(차단된 댓글)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    })
    @GetMapping("/comment/{commentId}")
    public CommentResponseDto getComment(@PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        return new CommentResponseDto(comment);
    }

    @Operation(summary = "댓글 수정",
            description = "댓글 수정에 관한 데이터들을 전달받아 `commentId`에 해당하는 댓글을 수정합니다.\n\n" +
                    "<del>작성자만 수정이 가능합니다. (미구현)</del>")
    @PatchMapping("/comment/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestBody @Valid UpdateCommentRequestDto request,
                              @Parameter(hidden = true) @Login Long loginMemberId) {
        commentService.updateComment(loginMemberId, commentId, request.getContent());
    }

    @Operation(summary = "댓글 삭제",
            description = "`commentId`에 해당하는 댓글을 삭제합니다.\n\n" +
                    "실제로 DB에서 삭제되지는 않고 \"삭제된 상태\"(`status=REMOVED`)로 변합니다.\n\n" +
                    "<del>작성자만 삭제가 가능합니다. (미구현)</del>")
    @DeleteMapping("/comment/{commentId}")
    public void removeComment(@PathVariable Long commentId,
                              @Parameter(hidden = true) @Login Long loginMemberId) {
        commentService.removeComment(loginMemberId, commentId);
    }

    @Operation(summary = "댓글 좋아요 추가",
            description = "로그인 사용자로 댓글 좋아요 등록.")
    @PostMapping("/comment/{commentId}/like")
    public void addCommentLike(@PathVariable Long commentId,
                               @Parameter(hidden = true) @Login Long loginMemberId) {
        commentLikeService.addLike(loginMemberId, commentId);
    }

    @Operation(summary = "댓글 좋아요 취소",
            description = "로그인 사용자가 댓글에 했던 좋아요 취소.")
    @DeleteMapping("/comment/{commentId}/like")
    public void cancelCommentLike(@PathVariable Long commentId,
                                  @Parameter(hidden = true) @Login Long loginMemberId) {
        commentLikeService.cancelLike(loginMemberId, commentId);
    }
}