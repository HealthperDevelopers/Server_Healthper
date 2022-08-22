package umc.healthper.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostStatus;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findById(Long id);

    @EntityGraph(attributePaths = {"member"})
    Page<Post> findAllByStatusNot(Pageable pageable, PostStatus postStatus);
}
