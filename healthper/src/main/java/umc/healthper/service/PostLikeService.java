package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.Member;
import umc.healthper.domain.Post;
import umc.healthper.domain.like.PostLike;
import umc.healthper.repository.MemberRepository;
import umc.healthper.repository.like.PostLikeRepository;
import umc.healthper.repository.post.PostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    /**
     * 좋아요 추가
     */
    public boolean addLike(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();

        validateAlreadyLike(member, post);

        PostLike postLike = PostLike.createPostLike(member, post);
        postLikeRepository.save(postLike);
        return true;
    }

    private void validateAlreadyLike(Member member, Post post) {
        if (!postLikeRepository.findByMemberAndPost(member, post).isEmpty()) {
            throw new IllegalStateException("이미 좋아요 하셨습니다.");
        }
    }
}
