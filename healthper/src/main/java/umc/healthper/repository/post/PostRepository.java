package umc.healthper.repository.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.post.Post;

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
}
