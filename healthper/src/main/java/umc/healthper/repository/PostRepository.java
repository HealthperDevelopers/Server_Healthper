package umc.healthper.repository;

import org.springframework.stereotype.Repository;
import umc.healthper.domain.Member;
import umc.healthper.domain.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepository {

    @PersistenceContext
    EntityManager em;
    private static Map<Integer, Post> store = new HashMap<>();
    private static long sequence = 0;

    // 게시글 등록
    public void save(Post post) {
        em.persist(post);
        post.setId(++sequence);
    }

    // 게시글 조회 - Id로 조회
    public Post findById(int postId) {
        return em.find(Post.class, postId);
    }

    // 게시글 조회 - 전체 게시글 리스트 조회 (Paging 적용 필요. 한 페이지에 개의 게시글)
    public List<Post> findPostList(int pageNum) {
        return em.createQuery("select p from Post p order by p.createdDate desc", Post.class)
                .setFirstResult(10 * (pageNum-1))
                .setMaxResult(10)
                .getResultList();
    }

    // 게시글 수정

    // 게시글 삭제
    public void removeById(int postId) {
    }
}
