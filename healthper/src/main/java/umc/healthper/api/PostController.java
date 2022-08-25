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
import umc.healthper.exception.ExceptionResponse;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.MemberService;
import umc.healthper.service.PostService;

import javax.validation.Valid;

@Tag(name = "Post", description = "게시글 API")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @Operation(summary = "게시글 생성",
            description = "게시글 정보를 받아 새로운 게시글을 생성합니다.\n\n" +
                    "**Request**\n\n" +
                    "- `postType`: `NORMAL`(일반), `QUESTION`(질문), `AUDIO`(음성, 음악)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CreatePostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/post")
    public CreatePostResponseDto savePost(@RequestBody @Valid CreatePostRequestDto requestDto,
                                          @Parameter(hidden = true) @Login Long loginMemberId) {
        Member member = memberService.findById(loginMemberId);

        PostType postType = PostType.transferFromString(requestDto.getPostType());
        Post post = Post.createPost(member, postType, requestDto.getTitle(), requestDto.getContent());

        Long id = postService.savePost(post).getId();
        return new CreatePostResponseDto(id);
    }

    @Operation(summary = "게시글 조회",
            description = "`posdId`에 해당하는 게시글을 조회합니다. 댓글과 대댓글 목록도 함께 반환됩니다.\n\n" +
                    "**Response**\n\n" +
                    "- `postType`: `NORMAL`(일반), `QUESTION`(질문), `AUDIO`(음성, 음악)\n\n" +
                    "- `postStatus`: `NORMAL`, `REMOVED`(삭제된 게시글), `BLOCKED`(차단된 게시글)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/post/{postId}")
    public PostResponseDto getPostInfo(@PathVariable Long postId) {
        Post findPost = postService.findPost(postId);
        return new PostResponseDto(findPost);
    }

    @Operation(summary = "게시글 목록 조회",
            description = "게시글 목록을 조회합니다. Paging(30개), Sorting이 지원됩니다.\n\n" +
                    "삭제된 게시글(`status=REMOVED`)과 차단된 게시글(`status=BLOCKED`)은 제외하고 조회합니다.\n\n" +
                    "응답 데이터는 약간 수정될 수 있습니다.\n\n" +
                    "**Request**\n\n" +
                    "- `sort`: `LATEST`(최신순), `LIKE`(추천순), `COMMENT`(댓글순)\n\n" +
                    "- `page`: 페이지 번호. 0부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PostListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @GetMapping("/posts")
    public PostListResponseDto getPostList(@RequestParam(value = "sort", defaultValue = "LATEST") PostSortingCriteria sortingCriteria,
                                           @RequestParam(defaultValue = "0") Integer page) {
        PostListResponseDto res = new PostListResponseDto();
        postService.findPostList(sortingCriteria, page)
                .forEach(post -> res.getContent().add(new ListPostResponseDto(post)));
        return res;
    }

    @Operation(summary = "게시글 수정",
            description = "게시글 수정에 관한 데이터들을 전달받아 `postId`에 해당하는 게시글을 수정합니다.\n\n" +
                    "작성자만 수정이 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/post/{postId}")
    public void updatePost(@PathVariable Long postId,
                           @RequestBody @Valid UpdatePostRequestDto request,
                           @Parameter(hidden = true) @Login Long loginMemberId) {
        postService.updatePost(loginMemberId, postId, request);
    }

    @Operation(summary = "게시글 삭제",
            description = "`postId`에 해당하는 게시글을 삭제합니다.\n\n" +
                    "실제로 DB에서 삭제되지는 않고 \"삭제된 상태\"(`postStatus=REMOVED`)로 변합니다.\n\n" +
                    "작성자만 삭제가 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/post/{postId}")
    public void removePost(@PathVariable Long postId,
                           @Parameter(hidden = true) @Login Long loginMemberId) {
        postService.removePost(loginMemberId, postId);
    }
}