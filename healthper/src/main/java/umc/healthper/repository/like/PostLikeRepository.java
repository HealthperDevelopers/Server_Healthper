package umc.healthper.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.Member;
import umc.healthper.domain.Post;
import umc.healthper.domain.like.PostLike;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    boolean existsByMemberAndPost(Member member, Post post);
}
