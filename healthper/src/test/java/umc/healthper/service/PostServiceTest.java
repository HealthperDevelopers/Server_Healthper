package umc.healthper.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostType;
import umc.healthper.dto.post.UpdatePostRequestDto;
import umc.healthper.exception.post.PostAlreadyRemovedException;

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
    public void 게시글_등록() {
        // given
        Member member = Member.createMember(100L, "woogie");
        memberService.join(member);
        Post post = Post.createPost(member, PostType.NORMAL, "제목1", "테스트입니다");
        postService.savePost(post);

        // when
        Post findPost = postService.findPost(post.getId());

        // then
        assertThat(post).isEqualTo(findPost);
    }

//    @Test
//    public void 게시글_목록_조회() throws Exception {
//    }

    @Test
    public void 게시글_수정() {
        // given
        Member member = Member.createMember(100L, "woogie");
        memberService.join(member);
        Post post = Post.createPost(member, PostType.NORMAL, "제목1", "테스트입니다");
        postService.savePost(post);

        // when
        postService.updatePost(post.getId(), new UpdatePostRequestDto("수정테스트", "수정이 잘 될까?"));

        // then
        Post findPost = postService.findPost(post.getId());

        assertThat(findPost.getTitle()).isEqualTo("수정테스트");
        assertThat(findPost.getContent()).isEqualTo("수정이 잘 될까?");
    }

    @Test(expected = PostAlreadyRemovedException.class)
    public void 게시글_삭제() {
        // given
        Member member = Member.createMember(100L, "woogie");
        memberService.join(member);
        Post post = Post.createPost(member, PostType.NORMAL, "제목1", "테스트입니다");
        postService.savePost(post);

        Long postId = post.getId();

        // when
        postService.removePost(postId);
        postService.findPost(postId);

        // then
        fail("게시글이 삭제되었기 때문에 Exception이 발생해야 한다.");
    }
}