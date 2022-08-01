package umc.healthper.repository;

import lombok.RequiredArgsConstructor;
import umc.healthper.domain.Post;
import umc.healthper.domain.PostStatus;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final EntityManager em;

    @Override
    public void removePost(Long id) {
        Post findPost = em.find(Post.class, id);
        // 댓글들도 삭제해야 하는가?
        // -> 아니다. 댓글정보 클릭해서 게시글 조회하려고 할 때는 "삭제된 게시글입니다"만 띄워주면 될 것
        findPost.setPostStatus(PostStatus.REMOVED);
    }
}
