package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.dto.post.*;
import umc.healthper.exception.ExceptionResponse;
import umc.healthper.service.MemberService;
import umc.healthper.service.PostService;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.*;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @Operation(summary = "게시글 생성",
            description = "게시글 정보를 받아 새로운 게시글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CreatePostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/post")
    public CreatePostResponseDto savePost(@RequestBody @Valid CreatePostRequestDto request) {
        Member member = memberService.findById(request.getMemberId());
        Post post = Post.createPost(member, request.getTitle(), request.getContent());

        Long id = postService.savePost(post).getId();
        return new CreatePostResponseDto(id);
    }

    @Operation(summary = "게시글 조회",
            description = "`posdId`에 해당하는 게시글을 조회합니다. 댓글과 대댓글 목록도 함께 반환됩니다.\n\n" +
                    "`postStatus`: `NORMAL`, `REMOVED`(삭제된 게시글), `BLOCKED`(차단된 게시글)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/post/{postId}")
    public PostResponseDto getPostInfo(@PathVariable Long postId) {
        Post findPost = postService.findPost(postId);
        return new PostResponseDto(findPost);
    }

    @Operation(summary = "게시글 목록 조회",
            description = "게시글 목록을 조회합니다. Paging, Sorting이 지원됩니다.\n\n" +
                    "해당 API는 수정할 예정이라 참고만 해주세요. 입력 값이랑 응답 데이터 전부 수정될 것 같습니다.\n\n" +
                    "**Query Parameter**\n\n" +
                    "- `page`: 페이지 번호 (0부터 시작, 기본값 0)\n\n" +
                    "- `size`: 한 페이지에 받을 데이터 개수 (기본값 30)\n\n" +
                    "- Example\n\n" +
                    "    - <del>`/posts`: 가장 최근 30개의 게시글 목록 조회</del>\n\n" +
                    "    - **`/posts?page=5`: 6번째 page의 게시글 목록 조회 (되도록이면 이 방법만 사용하시면 될 것 같습니다)**\n\n" +
                    "    - **`/posts?page=0`, `/posts`: 가장 최근 30개 게시글 목록 조회**\n\n" +
                    "    - <del>`/posts?page=0&size=10`: 가장 최근 10개의 게시글 목록 조회</del>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @GetMapping("/posts")
    public Page<ListPostResponseDto> getPosts(@PageableDefault(size = 30, sort = "createdAt", direction = DESC) Pageable pageable) {
        return postService.findPosts(pageable)
                .map(ListPostResponseDto::new);
    }

    @Operation(summary = "게시글 수정",
            description = "게시글 수정에 관한 데이터들을 전달받아 `postId`에 해당하는 게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/post/{postId}")
    public void updatePost(@PathVariable Long postId,
                           @RequestBody @Valid UpdatePostRequestDto request) {
        postService.updatePost(postId, request);
    }

    @Operation(summary = "게시글 삭제",
            description = "`postId`에 해당하는 게시글을 삭제합니다.\n\n" +
                    "실제로 DB에서 삭제되지는 않고 \"삭제된 상태\"(`postStatus=REMOVED`)로 변합니다.\n\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/post/{postId}")
    public void removePost(@PathVariable Long postId) {
        postService.removePost(postId);
    }
}