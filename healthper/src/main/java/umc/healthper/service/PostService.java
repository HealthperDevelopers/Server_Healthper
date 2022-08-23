package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostStatus;
import umc.healthper.dto.post.UpdatePostRequestDto;
import umc.healthper.exception.post.PostAlreadyRemovedException;
import umc.healthper.exception.post.PostNotFoundException;
import umc.healthper.exception.post.PostUnauthorizedException;
import umc.healthper.repository.post.PostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * Post 등록(저장)
     *
     * @param post
     * @return 생성된 Post 객체 return
     */
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    /**
     * Post 목록 조회 - Paging
     * 삭제되지 않은 게시글만 조회
     *
     * @param pageable
     * @return Post 객체들이 담긴 Page 객체 return
     */
    public Page<Post> findPosts(Pageable pageable) {
        return postRepository.findAllByStatusNot(pageable, PostStatus.REMOVED);
    }

    /**
     * Post 조회 - id
     *
     * @param postId
     * @return 조회된 Post 객체 return
     */
    public Post findPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        validateRemovedPost(post);
        post.addViewCount();
        return post;
    }

    /**
     * Post 수정
     *
     * @param postId
     * @param updateDto
     */
    @Transactional
    public void updatePost(Long loginMemberId, Long postId, UpdatePostRequestDto updateDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostAlreadyRemovedException::new);

        validatePostAuthority(loginMemberId, post);
        validateRemovedPost(post);

        post.update(updateDto.getTitle(), updateDto.getContent());
    }

    /**
     * Post 삭제
     *
     * @param postId
     */
    @Transactional
    public void removePost(Long loginMemberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostAlreadyRemovedException::new);

        validatePostAuthority(loginMemberId, post);
        validateRemovedPost(post);

        postRepository.removePost(post);
    }

    /**
     * 삭제된 게시글인지 검증. 삭제된 게시글이라면 PostAlreadyRemovedException throw
     *
     * @param post
     */
    private void validateRemovedPost(Post post) {
        if (post.getStatus() == PostStatus.REMOVED) {
            throw new PostAlreadyRemovedException();
        }
    }

    /**
     * 게시글에 대한 수정/삭제 권한이 있는지 검증. 작성자에게만 write 권한이 부여된다.
     * 게시글의 수정/삭제 권한이 없는 사용자라면 PostUnauthorizedException throw
     *
     * @param memberId
     * @param post
     */
    private void validatePostAuthority(Long memberId, Post post) {
        if (!memberId.equals(post.getMember().getId())) {
            throw new PostUnauthorizedException();
        }
    }
}
