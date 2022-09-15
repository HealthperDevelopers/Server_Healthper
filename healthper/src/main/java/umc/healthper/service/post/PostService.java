package umc.healthper.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostStatus;
import umc.healthper.domain.post.PostType;
import umc.healthper.dto.post.PostSortingCriteria;
import umc.healthper.dto.post.UpdatePostRequestDto;
import umc.healthper.exception.member.MemberNotFoundByIdException;
import umc.healthper.exception.post.PostAlreadyRemovedException;
import umc.healthper.exception.post.PostNotFoundException;
import umc.healthper.exception.post.PostUnauthorizedException;
import umc.healthper.repository.MemberRepository;
import umc.healthper.repository.post.PostRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * Post 등록(저장)
     *
     * @param post 등록할 Post 객체
     * @return 생성된 Post 객체 return
     */
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    /**
     * Post 목록 조회 - Paging
     * 삭제된 게시글, 차단한 Member 게시글은 제외하고 조회
     *
     * @param sort 정렬 기준
     * @param page 페이지 번호
     * @param loginMemberId 로그인중인 Member의 id
     * @return Post List
     */
    public List<Post> findPosts(PostType postType, PostSortingCriteria sort, Integer page, Long loginMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundByIdException::new);
        return postRepository.findPosts(postType, sort, page, loginMember);
    }

    /**
     * Post 조회 - id
     *
     * @param postId 조회할 Post의 id
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
     * @param postId 수정할 Post의 id
     * @param dto 수정 내용이 담긴 DTO
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
     * @param postId 삭제할 Post id
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
     * @param post 검증할 Post id
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
     * @param memberId 수정/삭제 권한 여부를 확인할 Member의 id
     * @param post 수정/삭제할 Post
     */
    private void validatePostAuthority(Long memberId, Post post) {
        if (!memberId.equals(post.getMember().getId())) {
            throw new PostUnauthorizedException();
        }
    }
}
