package umc.healthper.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostLike;
import umc.healthper.exception.member.MemberNotFoundByIdException;
import umc.healthper.exception.post.PostNotFoundException;
import umc.healthper.exception.postlike.PostLikeAlreadyExistException;
import umc.healthper.exception.postlike.PostLikeNotFoundException;
import umc.healthper.repository.MemberRepository;
import umc.healthper.repository.post.PostLikeRepository;
import umc.healthper.repository.post.PostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    /**
     * 게시글 좋아요 추가
     *
     * @param memberId 좋아요한 Member의 id
     * @param postId   좋아요한 Post의 id
     */
    @Transactional
    public void addLike(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundByIdException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        validateAlreadyLike(member, post);

        PostLike postLike = PostLike.createPostLike(member, post);
        postLikeRepository.save(postLike);
    }

    /**
     * 게시글 좋아요 취소
     *
     * @param memberId 좋아요한 Member의 id
     * @param postId   좋아요한 Post의 id
     */
    @Transactional
    public void cancelLike(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundByIdException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post)
                .orElseThrow(PostLikeNotFoundException::new);
        member.getPostLikes().remove(postLike);
        post.getLikes().remove(postLike);
        postLikeRepository.delete(postLike);
    }

    private void validateAlreadyLike(Member member, Post post) {
        if (postLikeRepository.existsByMemberAndPost(member, post)) {
            throw new PostLikeAlreadyExistException();
        }
    }
}
