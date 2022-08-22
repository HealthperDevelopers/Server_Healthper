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
     */
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    /**
     * Post 목록 조회 - Paging
     * 삭제되지 않은 게시글만 조회
     */
    public Page<Post> findPosts(Pageable pageable) {
        return postRepository.findAllByStatusNot(pageable, PostStatus.REMOVED);
    }

    /**
     * Post 조회 - id
     */
    public Post findPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.addViewCount();
        return post;
    }

    /**
     * Post 수정
     */
    @Transactional
    public void updatePost(Long postId, UpdatePostRequestDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(PostAlreadyRemovedException::new);
        validateAlreadyRemovedPost(post);
        post.update(postDto.getTitle(), postDto.getContent());
    }

    /**
     * Post 삭제
     */
    @Transactional
    public void removePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostAlreadyRemovedException::new);
        validateAlreadyRemovedPost(post);
        postRepository.removePost(post);
    }

    // 이미 삭제된 게시글인지 검증
    private void validateAlreadyRemovedPost(Post post) {
        if (post.getStatus() == PostStatus.REMOVED) {
            throw new PostAlreadyRemovedException();
        }
    }
}
