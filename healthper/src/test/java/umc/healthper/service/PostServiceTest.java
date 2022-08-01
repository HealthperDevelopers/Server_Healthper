package umc.healthper.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.Member;
import umc.healthper.domain.MemberStatus;
import umc.healthper.domain.Post;
import umc.healthper.dto.post.UpdatePostDto;

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
    public void 게시글_등록() throws Exception {
        // given
        Member member = createMember(100L, "woogie");
        memberService.join(member);
        Post post = new Post(member, "제목1", "테스트입니다");
        postService.savePost(post);

        // when
        Post findPost = postService.findById(post.getId());

        // then
        assertThat(post).isEqualTo(findPost);
    }

//    @Test
//    public void 게시글_목록_조회() throws Exception {
//        // given
//        Member member = createMember(100L, "우기");
//        memberService.join(member);
//        Post post1 = new Post(member, "제목1", "테스트입니다");
//        postService.savePost(post1);
//        Post post2 = new Post(member, "제목2", "테스트입니다");
//        postService.savePost(post2);
//        Post post3 = new Post(member, "제목3", "테스트입니다");
//        postService.savePost(post3);
//
//        // when
//        List<Post> posts = postService.findPosts(1);
//
//        // then
//        assertThat(posts.size()).isEqualTo(3);
//    }

    @Test
    public void 게시글_수정() throws Exception {
        // given
        Member member = createMember(100L, "woogie");
        memberService.join(member);
        Post post = new Post(member, "제목1", "테스트입니다");
        postService.savePost(post);

        // when
        postService.updatePost(post.getId(), new UpdatePostDto("수정테스트", "수정이 잘 될까?"));

        // then
        Post findPost = postService.findById(post.getId());

        assertThat(findPost.getTitle()).isEqualTo("수정테스트");
        assertThat(findPost.getContent()).isEqualTo("수정이 잘 될까?");
    }

    @Test(expected = IllegalStateException.class)
    public void 게시글_삭제() throws Exception {
        // given
        Member member = createMember(100L, "woogie");
        memberService.join(member);
        Post post = new Post(member, "제목1", "테스트입니다");
        postService.savePost(post);

        Long postId = post.getId();

        // when
        postService.removePost(postId);
        postService.findById(postId);

        // then
        fail("게시글이 삭제되었기 때문에 Exception이 발생해야 한다.");
    }

    private Member createMember(Long kakaoKey, String nickName) {
        Member member = new Member();
        member.setKakaoKey(kakaoKey);
        member.setNickname(nickName);
        member.setReportedCount(0);
        member.setStatus(MemberStatus.NORMAL);
        return member;
    }
}