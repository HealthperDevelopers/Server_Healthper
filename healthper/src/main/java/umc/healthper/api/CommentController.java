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
import umc.healthper.dto.comment.CreateCommentResponseDto;
import umc.healthper.dto.comment.CreateCommentRequestDto;
import umc.healthper.dto.comment.UpdateCommentRequestDto;
import umc.healthper.exception.ExceptionResponse;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.CommentService;
import umc.healthper.service.MemberService;
import umc.healthper.service.PostService;

import javax.validation.Valid;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;


    @Operation(summary = "댓글 생성",
            description = "댓글 정보를 받아 새로운 댓글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CreateCommentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/comment")
    public CreateCommentResponseDto saveComment(@RequestBody @Valid CreateCommentRequestDto request,
                                                @Parameter(hidden = true) @Login Long loginMemberId) {
        Member findMember = memberService.findById(loginMemberId);
        Post findPost = postService.findPost(request.getPostId(), false);
        Comment comment = Comment.createComment(findMember, findPost, request.getContent());

        Long parentId = request.getParentId();
        if (parentId != null) {
            Comment parentComment = commentService.findById(parentId);
            parentComment.addChildComment(comment);
        }

        Long id = commentService.saveComment(comment).getId();
        return new CreateCommentResponseDto(id);
    }

    @Operation(summary = "댓글 조회",
            description = "`commentId`에 해당하는 댓글을 조회합니다.\n\n" +
                    "**Response**\n\n" +
                    "- `status`: `NORMAL`, `REMOVED`(삭제된 댓글), `BLOCKED`(차단된 댓글)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CommentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/comment/{commentId}")
    public CommentResponseDto getComment(@PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        return new CommentResponseDto(comment);
    }

    @Operation(summary = "댓글 수정",
            description = "댓글 수정에 관한 데이터들을 전달받아 `commentId`에 해당하는 댓글을 수정합니다.\n\n" +
                    "<del>작성자만 수정이 가능합니다. (미구현)</del>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/comment/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestBody @Valid UpdateCommentRequestDto request,
                              @Parameter(hidden = true) @Login Long loginMemberId) {
        commentService.updateComment(commentId, request.getContent());
    }

    @Operation(summary = "댓글 삭제",
            description = "`commentId`에 해당하는 댓글을 삭제합니다.\n\n" +
                    "실제로 DB에서 삭제되지는 않고 \"삭제된 상태\"(`postStatus=REMOVED`)로 변합니다.\n\n" +
                    "<del>작성자만 삭제가 가능합니다. (미구현)</del>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/comment/{commentId}")
    public void removeComment(@PathVariable Long commentId,
                              @Parameter(hidden = true) @Login Long loginMemberId) {
        commentService.removeComment(commentId);
    }
}