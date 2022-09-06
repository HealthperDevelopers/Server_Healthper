package umc.healthper.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostLike;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    /**
     * Member와 Post에 해당하는 PostLike 객체 조회
     *
     * @param member 좋아요한 Member
     * @param post   좋아요한 Post
     * @return 조회된 PostLike 객체
     */
    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    /**
     * Member와 Post에 해당하는 PostLike 존재 여부 조회
     *
     * @param member 좋아요한 Member
     * @param post   좋아요한 Post
     * @return PostLike 객체 존재 여부
     */
    boolean existsByMemberAndPost(Member member, Post post);
}
