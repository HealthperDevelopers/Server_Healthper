package umc.healthper.repository.post;

import umc.healthper.domain.post.Post;

public interface PostRepositoryCustom {
    
    /**
     * 전달받은 post 객체를 삭제된 상태(postStatus=REMOVED)로 변경
     * @param post
     */
    void removePost(Post post);
}
