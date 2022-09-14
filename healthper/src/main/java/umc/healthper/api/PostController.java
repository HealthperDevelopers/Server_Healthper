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
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostType;
import umc.healthper.dto.post.*;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.MemberService;
import umc.healthper.service.post.PostLikeService;
import umc.healthper.service.post.PostService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Tag(name = "Post", description = "게시글 API")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final MemberService memberService;

    @Operation(summary = "게시글 생성",
            description = "게시글 정보를 받아 새로운 게시글을 생성합니다.\n\n" +
                    "**Request**\n\n" +
                    "- `type`: `NORMAL`(일반), `QUESTION`(질문), `AUDIO`(음성, 음악)")
    @PostMapping("/post")
    public CreatePostResponseDto savePost(@RequestBody @Valid CreatePostRequestDto requestDto,
                                          @Parameter(hidden = true) @Login Long loginMemberId) {
        Member member = memberService.findById(loginMemberId);

        Post post = Post.createPost(member, requestDto.getType(), requestDto.getTitle(), requestDto.getContent());

        return new CreatePostResponseDto(postService.savePost(post).getId());
    }

    @Operation(summary = "게시글 조회",
            description = "`posdId`에 해당하는 게시글을 조회합니다. 댓글과 대댓글 목록도 함께 반환됩니다.\n\n" +
                    "**Response**\n\n" +
                    "- `type`: `NORMAL`(일반), `QUESTION`(질문), `AUDIO`(음성, 음악)\n\n" +
                    "- `status`: `NORMAL`, `REMOVED`(삭제된 게시글), `BLOCKED`(차단된 게시글)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PostResponseDto.class)))
    })
    @GetMapping("/post/{postId}")
    public PostResponseDto viewPost(@PathVariable Long postId,
                                    @Parameter(hidden = true) @Login Long loginMemberId) {
        Post post = postService.findPost(postId, true);
        Member loginMember = memberService.findById(loginMemberId);
        return new PostResponseDto(post, loginMember);
    }

    @Operation(summary = "게시글 목록 조회",
            description = "게시글 목록을 조회합니다. Paging(30개), Sorting이 지원됩니다.\n\n" +
                    "삭제된 게시글(`status=REMOVED`)과 신고로 차단된 게시글(`status=BLOCKED`)은 제외하고 조회합니다.\n\n" +
                    "차단한 회원의 게시글도 제외하고 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PostSliceResponseDto.class)))
    })
    @GetMapping("/posts")
    public PostListResponseDto getPosts(@RequestParam(value = "type", defaultValue = "NORMAL") PostType type,
                                        @RequestParam(value = "sort", defaultValue = "LATEST") PostSortingCriteria sort,
                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                        @Parameter(hidden = true) @Login Long loginMemberId) {
        return new PostListResponseDto(
                postService.findPosts(type, sort, page, loginMemberId).stream()
                        .map(ListPostResponseDto::new)
                        .collect(Collectors.toList())
        );
    }

    @Operation(summary = "게시글 수정",
            description = "게시글 수정에 관한 데이터들을 전달받아 `postId`에 해당하는 게시글을 수정합니다.\n\n" +
                    "작성자만 수정이 가능합니다.")
    @PatchMapping("/post/{postId}")
    public void updatePost(@PathVariable Long postId,
                           @RequestBody @Valid UpdatePostRequestDto request,
                           @Parameter(hidden = true) @Login Long loginMemberId) {
        postService.updatePost(loginMemberId, postId, request);
    }

    @Operation(summary = "게시글 삭제",
            description = "`postId`에 해당하는 게시글을 삭제합니다.\n\n" +
                    "실제로 DB에서 삭제되지는 않고 \"삭제된 상태\"(`status=REMOVED`)로 변합니다.\n\n" +
                    "작성자만 삭제가 가능합니다.")
    @DeleteMapping("/post/{postId}")
    public void removePost(@PathVariable Long postId,
                           @Parameter(hidden = true) @Login Long loginMemberId) {
        postService.removePost(loginMemberId, postId);
    }

    @Operation(summary = "게시글 좋아요 추가",
            description = "로그인 사용자로 게시글 좋아요 등록.")
    @PostMapping("/post/{postId}/like")
    public void addPostLike(@Parameter(hidden = true) @Login Long loginMemberId,
                            @PathVariable Long postId) {
        postLikeService.addLike(loginMemberId, postId);
    }

    @Operation(summary = "게시글 좋아요 취소",
            description = "로그인 사용자가 게시글에 했던 좋아요 취소.")
    @DeleteMapping("/post/{postId}/like")
    public void cancelPostLike(@Parameter(hidden = true) @Login Long loginMemberId,
                               @PathVariable Long postId) {
        postLikeService.cancelLike(loginMemberId, postId);
    }
}