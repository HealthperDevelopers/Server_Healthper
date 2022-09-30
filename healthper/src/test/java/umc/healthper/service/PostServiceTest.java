package umc.healthper.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostType;
import umc.healthper.dto.post.CreatePostRequestDto;
import umc.healthper.dto.post.UpdatePostRequestDto;
import umc.healthper.exception.post.PostAlreadyRemovedException;
import umc.healthper.exception.post.PostUnauthorizedException;
import umc.healthper.service.post.PostService;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("게시글 등록")
    public void savePost() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);
        Long postId = postService.savePost(member.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));
        Post post = postService.findPost(postId);

        // when
        Post findPost = postService.findPost(post.getId());

        // then
        assertThat(post).isEqualTo(findPost);
    }

//    @Test
//    public void 게시글_목록_조회() throws Exception {
//    }

    @Test
    @DisplayName("게시글 수정")
    public void updatePost() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);
        Long postId = postService.savePost(member.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));
        Post post = postService.findPost(postId);

        // when
        postService.updatePost(member.getId(), post.getId(), new UpdatePostRequestDto("수정테스트", "수정이 잘 될까?"));

        // then
        Post findPost = postService.findPost(post.getId());

        assertThat(findPost.getTitle()).isEqualTo("수정테스트");
        assertThat(findPost.getContent()).isEqualTo("수정이 잘 될까?");
    }

    @Test(expected = PostUnauthorizedException.class)
    @DisplayName("게시글 수정 시 권한 예외 발생")
    public void updatePostUnauthorizedException() {
        // given
        memberService.joinMember(100L, "회원1");
        Member member1 = memberService.findByKakaoKey(100L);
        memberService.joinMember(101L, "회원2");
        Member member2 = memberService.findByKakaoKey(101L);

        Long postId = postService.savePost(member1.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));
        Post post = postService.findPost(postId);

        // when
        postService.updatePost(member2.getId(), post.getId(), new UpdatePostRequestDto("수정테스트", "수정이 잘 될까?"));

        // then
        fail("작성자가 아닌 사용자가 게시글을 수정하려 했기 때문에 exception이 발생한다.");
    }

    @Test(expected = PostAlreadyRemovedException.class)
    @DisplayName("게시글 삭제")
    public void removePost() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);
        Long postId = postService.savePost(member.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));

        // when
        postService.removePost(member.getId(), postId);
        postService.findPost(postId);

        // then
        fail("게시글이 삭제되었기 때문에 Exception이 발생해야 한다.");
    }

    @Test(expected = PostUnauthorizedException.class)
    @DisplayName("게시글 삭제 시 권한 예외 발생")
    public void removePostUnauthorizedException() {
        // given
        memberService.joinMember(100L, "회원1");
        Member member1 = memberService.findByKakaoKey(100L);
        memberService.joinMember(101L, "회원2");
        Member member2 = memberService.findByKakaoKey(101L);

        Long postId = postService.savePost(member1.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));

        // when
        postService.removePost(member2.getId(), postId);

        // then
        fail("작성자가 아닌 사용자가 게시글을 삭제하려 했기 때문에 exception이 발생한다.");
    }
}