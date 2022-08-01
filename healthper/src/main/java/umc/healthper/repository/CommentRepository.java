package umc.healthper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
