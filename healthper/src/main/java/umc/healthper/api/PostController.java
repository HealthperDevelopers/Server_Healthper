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

        PostType postType = PostType.transferFromString(requestDto.getType());
        Post post = Post.createPost(member, postType, requestDto.getTitle(), requestDto.getContent());

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
    public PostResponseDto viewPost(@PathVariable Long postId) {
        Post post = postService.findPost(postId, true);
        return new PostResponseDto(post);
    }

    @Operation(summary = "게시글 목록 조회",
            description = "게시글 목록을 조회합니다. Paging(30개), Sorting이 지원됩니다.\n\n" +
                    "삭제된 게시글(`status=REMOVED`)과 차단된 게시글(`status=BLOCKED`)은 제외하고 조회합니다.\n\n" +
                    "응답 데이터는 약간 수정될 수 있습니다.\n\n" +
                    "<br>**Request**\n\n" +
                    "- `sort`: `LATEST`(최신순), `LIKE`(추천순), `COMMENT`(댓글순)\n\n" +
                    "- `page`: 페이지 번호. 0부터 시작합니다.\n\n" +
                    "<br>**Response**\n\n" +
                    "- `content`: 게시글 리스트\n\n" +
                    "- `sort`: 정렬 기준\n\n" +
                    "- `pageNum`: 페이지 번호\n\n" +
                    "- `size`: 한 페이지에 담기는 데이터(게시글)의 최대 개수\n\n" +
                    "- `numberOfElements`: 현재 페이지에 담긴 데이터(게시글)의 수\n\n" +
                    "- `first`: 첫 페이지인가?\n\n" +
                    "- `last`: 마지막 페이지인가?\n\n" +
                    "- `hasNext`: 다음 페이지가 존재하는지에 대한 여부")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PostSliceResponseDto.class)))
    })
    @GetMapping("/posts")
    public PostSliceResponseDto getPosts(@RequestParam(value = "sort", defaultValue = "LATEST") PostSortingCriteria sortingCriteria,
                                         @RequestParam(defaultValue = "0") Integer page) {
        return new PostSliceResponseDto(
                postService.findPosts(sortingCriteria, page).map(ListPostResponseDto::new),
                sortingCriteria
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