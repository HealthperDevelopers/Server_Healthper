package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import umc.healthper.domain.Post;
import umc.healthper.repository.PostRepository;

import java.util.List;

@Service
//@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * Post 등록(저장)
     */
    public int registration(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    /**
     * Post 조회
     */
//    @Transactional(readOnly = true)
    public List<Post> findPostList(int pageNum) {
        return postRepository.findPostList(pageNum);
    }

    public Post findPost(int postId) {
        return postRepository.findById(postId);
    }

    /**
     * Post 수정
     */

    /**
     * Post 삭제
     */
    public void removePost(int postId) {
        validateRemovedPost(postId);
        postRepository.removeById(postId);
    }

    private void validateRemovedPost(int postId) {
        Post findPost = postRepository.findById(postId);
        if (findPost == null) {
            throw new IllegalStateException("이미 삭제된 게시물입니다.");
        }
    }
}
