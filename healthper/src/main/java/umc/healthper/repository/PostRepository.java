package umc.healthper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.healthper.domain.Post;
import umc.healthper.domain.PostStatus;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    private final int NUMBER_OF_PAGING_POSTS = 30;

    /**
     * 게시글 등록
     */
    public void save(Post post) {
        em.persist(post);
    }

    /**
     * 게시글 조회
     */
    public Post findById(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findPosts(int page) {
        return em.createQuery("select p from Post p" +
                        " join fetch p.member" +
                        " where p.postStatus<>'REMOVED'" +
                        " order by p.createdAt desc", Post.class)
                .setFirstResult(NUMBER_OF_PAGING_POSTS * (page - 1))
                .setMaxResults(NUMBER_OF_PAGING_POSTS)
                .getResultList();
    }

    /**
     * 게시글 삭제
     */
    public void removeById(Long id) {
        Post findPost = em.find(Post.class, id);
        // 댓글들도 삭제해야 하는가?
        // -> 아니다. 댓글정보 클릭해서 게시글 조회하려고 할 때는 "삭제된 게시글입니다"만 띄워주면 될 것
        findPost.setPostStatus(PostStatus.REMOVED);
    }
}
