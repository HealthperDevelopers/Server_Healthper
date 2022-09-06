package umc.healthper.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostStatus;
import umc.healthper.dto.post.PostSortingCriteria;
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
    private final static int NUMBER_OF_PAGING = 30;

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

    /*
     * [JPQL] Post 목록 조회 - Paging
     * 삭제된 게시글, 차단된 게시글은 제외하고 조회
     *
     * @param sortingCriteria
     * @param page
     * @return Post 객체들이 담긴 List return
     */
//    public List<Post> findPostList(PostSortingCriteria sortingCriteria, Integer page) {
//        return postRepository.findPostList(sortingCriteria, page);
//    }

    /**
     * [Spring Data] Post 목록 조회 - Paging
     * 삭제된 게시글, 차단된 게시글은 제외하고 조회
     *
     * @param sortingCriteria
     * @param page
     * @return Post 객체들이 담긴 Slice 객체 return
     */
    public Slice<Post> findPosts(PostSortingCriteria sortingCriteria, Integer page) {
        Sort sort;
        switch (sortingCriteria) {
            case LATEST:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
            case LIKE:
                sort = Sort.by(Sort.Direction.DESC, "postLikeCount");
                break;
            case COMMENT:
                sort = Sort.by(Sort.Direction.DESC, "commentCount");
                break;
            default:
                throw new IllegalArgumentException("잘못된 정렬 기준입니다.");
        }

        PageRequest pageRequest = PageRequest.of(page, NUMBER_OF_PAGING, sort);
        return postRepository.findPostsByStatusNotAndStatusNot(pageRequest, PostStatus.REMOVED, PostStatus.BLOCKED);
    }

    /**
     * Post 조회 - id
     *
     * @param postId
     * @param isView 조회수 증가 여부
     * @return 조회된 Post 객체 return
     */
    public Post findPost(Long postId, boolean isView) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        validateRemovedPost(post);

        if (isView) {
            post.addViewCount();
        }

        return post;
    }

    /**
     * Post 수정
     *
     * @param postId
     * @param dto
     */
    @Transactional
    public void updatePost(Long loginMemberId, Long postId, UpdatePostRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostAlreadyRemovedException::new);

        validateRemovedPost(post);
        validatePostAuthority(loginMemberId, post);

        post.update(dto.getTitle(), dto.getContent());
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

        validateRemovedPost(post);
        validatePostAuthority(loginMemberId, post);

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
