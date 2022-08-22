package umc.healthper.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostStatus;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    /**
     * Post 단건 조회
     * - Member Entity Fetch Join
     * @param id must not be {@literal null}.
     * @return 조회된 Optional Post 객체
     */
    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findById(Long id);

    /**
     * Post 목록 조회
     * - Member Entity Fetch Join
     * - Paging, Sorting 기능 제공
     * @param pageable
     * @param postStatus
     * @return 조회된 Post 객체들이 담긴 Page 객체
     */
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findAllByStatusNot(Pageable pageable, PostStatus postStatus);
}
