package umc.healthper.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
