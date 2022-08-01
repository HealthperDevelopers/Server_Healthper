package umc.healthper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.Post;
import umc.healthper.domain.PostStatus;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @EntityGraph(attributePaths = {"member"})
    Page<Post> findAllByPostStatusNot(Pageable pageable, PostStatus postStatus);
}
