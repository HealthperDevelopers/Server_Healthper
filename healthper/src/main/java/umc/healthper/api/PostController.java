package umc.healthper.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import umc.healthper.domain.Member;
import umc.healthper.domain.Post;
import umc.healthper.dto.post.*;
import umc.healthper.service.MemberService;
import umc.healthper.service.PostService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    /**
     * 게시글 등록
     */
    @PostMapping("/post")
    public CreatePostResponseDto savePost(@RequestBody @Valid CreatePostRequestDto request) {
        Member member = memberService.findById(request.getMemberId());
        Post post = Post.createPost(member, request.getTitle(), request.getContent());

        Long id = postService.savePost(post).getId();
        return new CreatePostResponseDto(id);
    }

    /**
     * 게시글 조회
     */
    @GetMapping("/post/{postId}")
    public PostResponseDto getPostInfo(@PathVariable Long postId) {
        Post findPost = postService.findById(postId);
        return new PostResponseDto(findPost);
    }

    /**
     * 게시글 목록 조회
     */
    @GetMapping("/posts")
    public Page<ListPostResponseDto> getPosts(@PageableDefault(size = 30, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return (postService.findPosts(pageable).map(ListPostResponseDto::new));
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/post/{postId}")
    public void updatePost(@PathVariable Long postId,
                                      @RequestBody @Valid UpdatePostRequestDto request) {
        postService.updatePost(postId, request);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/post/{postId}")
    public void removePost(@PathVariable Long postId) {
        postService.removePost(postId);
    }
}