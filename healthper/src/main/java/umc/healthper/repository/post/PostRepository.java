package umc.healthper.repository.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostStatus;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    /**
     * Post 단건 조회
     * - Member Entity Fetch Join
     *
     * @param id must not be {@literal null}.
     * @return 조회된 Optional Post 객체
     */
    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findById(Long id);

    /**
     * 전달받은 Paging, Sorting 기준으로 정렬된 게시글 목록을 불러온다.
     * - Paging: 30개
     * - 정렬 기준: LATEST(최신순), LIKE(추천순), COMMENT(댓글순)
     *
     * @param pageable - Paging, Sorting 정보
     * @param removed - 게시글 삭제 상태
     * @param blocked - 게시글 차단 상태
     * @return Post 객체들이 담긴 Slice 객체
     */
    Slice<Post> findPostsByStatusNotAndStatusNot(Pageable pageable, PostStatus removed, PostStatus blocked);
}
