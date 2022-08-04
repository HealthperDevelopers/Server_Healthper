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
    @Transactional
    public void addLike(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();

        validateAlreadyLike(member, post);

        PostLike postLike = PostLike.createPostLike(member, post);
        postLikeRepository.save(postLike);
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void cancelLike(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();

        validateNotLike(member, post);

        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post).get();
        member.getPostLikes().remove(postLike);
        post.getLikes().remove(postLike);
        postLikeRepository.delete(postLike);
    }

    private void validateAlreadyLike(Member member, Post post) {
        if (postLikeRepository.existsByMemberAndPost(member, post)) {
            throw new IllegalStateException("이미 좋아요 했던 게시글입니다.");
        }
    }

    private void validateNotLike(Member member, Post post) {
        if (!postLikeRepository.existsByMemberAndPost(member, post)) {
            throw new IllegalStateException("좋아요 하지 않은 게시글은 좋아요를 취소할 수 없습니다.");
        }
    }
}
