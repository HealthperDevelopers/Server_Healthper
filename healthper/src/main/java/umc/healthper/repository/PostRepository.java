package umc.healthper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}
