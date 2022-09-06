package umc.healthper.repository.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.comment.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    /**
     * Comment 단건 조회 by id
     * - Member[, Post] Fetch join
     *
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Comment> findById(Long aLong);
}
