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
import umc.healthper.repository.post.PostRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * Post 등록(저장)
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
     * @param pageable
     * @return Post 객체들이 담긴 Page 객체 return
     */
    public Page<Post> findPosts(Pageable pageable) {
        return postRepository.findAllByStatusNot(pageable, PostStatus.REMOVED);
    }

    /**
     * Post 조회 - id
     * @param postId
     * @return 조회된 Post 객체 return
     */
    public Post findPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        post.addViewCount();
        return post;
    }

    /**
     * Post 수정
     * @param postId
     * @param updatePostRequestDto
     */
    @Transactional
    public void updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostAlreadyRemovedException::new);
        validateAlreadyRemovedPost(post);
        post.update(updatePostRequestDto.getTitle(), updatePostRequestDto.getContent());
    }

    /**
     * Post 삭제
     * @param postId
     */
    @Transactional
    public void removePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostAlreadyRemovedException::new);
        validateAlreadyRemovedPost(post);
        postRepository.removePost(post);
    }

    /**
     * 이미 삭제된 게시글인지 검증. 이미 삭제되었다면 PostAlreadyRemovedException throw
     * @param post
     */
    private void validateAlreadyRemovedPost(Post post) {
        if (post.getStatus() == PostStatus.REMOVED) {
            throw new PostAlreadyRemovedException();
        }
    }
}
