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
import umc.healthper.exception.postlike.PostLikeAlreadyExistException;
import umc.healthper.exception.postlike.PostLikeNotFoundException;
import umc.healthper.service.post.PostLikeService;
import umc.healthper.service.post.PostService;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostLikeServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    PostLikeService postLikeService;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("게시글 좋아요 추가")
    public void addLike() {
        // given
        memberService.joinMember(100L, "회원1");
        Member member1 = memberService.findByKakaoKey(100L);
        memberService.joinMember(101L, "회원2");
        Member member2 = memberService.findByKakaoKey(101L);
        memberService.joinMember(102L, "회원3");
        Member member3 = memberService.findByKakaoKey(102L);

        Long post1Id = postService.savePost(member1.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));
        Post post1 = postService.findPost(post1Id);
        Long post2Id = postService.savePost(member1.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 2, "테스트를 위한 " + 2 + "번 게시글"));
        Post post2 = postService.findPost(post2Id);
        Long post3Id = postService.savePost(member1.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 3, "테스트를 위한 " + 3 + "번 게시글"));
        Post post3 = postService.findPost(post3Id);

        // when
        postLikeService.addLike(member1.getId(), post1.getId());
        postLikeService.addLike(member1.getId(), post2.getId());
        postLikeService.addLike(member2.getId(), post1.getId());
        postLikeService.addLike(member2.getId(), post3.getId());
        postLikeService.addLike(member3.getId(), post1.getId());

        // then
        assertThat(member1.getPostLikes().size()).isEqualTo(2);
        assertThat(member2.getPostLikes().size()).isEqualTo(2);
        assertThat(member3.getPostLikes().size()).isEqualTo(1);
        assertThat(post1.getLikes().size()).isEqualTo(3);
        assertThat(post2.getLikes().size()).isEqualTo(1);
        assertThat(post3.getLikes().size()).isEqualTo(1);
    }

    @Test(expected = PostLikeAlreadyExistException.class)
    @DisplayName("게시글 좋아요 중복 추가")
    public void addLikeAlreadyExistException() {
        // given
        memberService.joinMember(100L, "우기");
        Member member = memberService.findByKakaoKey(100L);

        Long postId = postService.savePost(member.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));
        Post post = postService.findPost(postId);

        // when
        postLikeService.addLike(member.getId(), post.getId());
        postLikeService.addLike(member.getId(), post.getId());

        // then
        fail("중복으로 좋아요 했기 때문에 예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("게시글 좋아요 취소")
    public void cancelLike() {
        // given
        memberService.joinMember(100L, "회원1");
        Member member1 = memberService.findByKakaoKey(100L);
        memberService.joinMember(101L, "회원2");
        Member member2 = memberService.findByKakaoKey(101L);

        Long post1Id = postService.savePost(member1.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));
        Post post1 = postService.findPost(post1Id);
        Long post2Id = postService.savePost(member1.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 2, "테스트를 위한 " + 2 + "번 게시글"));
        Post post2 = postService.findPost(post2Id);

        postLikeService.addLike(member1.getId(), post1.getId());
        postLikeService.addLike(member1.getId(), post2.getId());
        postLikeService.addLike(member2.getId(), post1.getId());

        // when
        postLikeService.cancelLike(member2.getId(), post1.getId());

        // then
        assertThat(member1.getPostLikes().size()).isEqualTo(2);
        assertThat(member2.getPostLikes().size()).isEqualTo(0);
        assertThat(post1.getLikes().size()).isEqualTo(1);
        assertThat(post2.getLikes().size()).isEqualTo(1);
    }

    @Test(expected = PostLikeNotFoundException.class)
    @DisplayName("좋아요 한 적 없는 게시글의 좋아요 취소")
    public void cancelLikeNeverLikeException() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);

        Long postId = postService.savePost(member.getId(), new CreatePostRequestDto(PostType.NORMAL, "제목" + 1, "테스트를 위한 " + 1 + "번 게시글"));
        Post post = postService.findPost(postId);

        // when
        postLikeService.cancelLike(member.getId(), post.getId());

        // then
        fail("좋아요 한 적 없는 게시글을 좋아요 취소하고 있기 때문에 예외가 발생해야 한다.");
    }
}