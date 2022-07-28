package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.Post;
import umc.healthper.domain.PostStatus;
import umc.healthper.dto.UpdatePostDto;
import umc.healthper.repository.PostRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * Post 등록(저장)
     */
    @Transactional
    public Long savePost(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    /**
     * Post 조회
     */
    public List<Post> findPosts(int page) {
        return postRepository.findPosts(page);
    }

    public Post findOne(Long postId) {
        validatePost(postId);
        return postRepository.findById(postId);
    }

    /**
     * Post 수정
     */
    @Transactional
    public void updatePost(Long postId, UpdatePostDto postDto) {
        validatePost(postId);
        Post post = postRepository.findById(postId);
        post.change(postDto.getTitle(), postDto.getContent());
    }

    /**
     * Post 삭제
     */
    @Transactional
    public void removePost(Long postId) {
        validatePost(postId);
        postRepository.removeById(postId);
    }

    // 존재하지 않는 게시글인지, 이미 삭제된 게시글인지 검증
    private Post validatePost(Long postId) {
        Post findPost = postRepository.findById(postId);
        if (findPost == null) {
            throw new IllegalStateException("존재하지 않는 게시글입니다.");
        }
        else if (findPost.getPostStatus() == PostStatus.REMOVED) {
            throw new IllegalStateException("이미 삭제된 게시글입니다.");
        }
        return findPost;
    }
}
