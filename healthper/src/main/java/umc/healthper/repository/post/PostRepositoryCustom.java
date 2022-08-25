package umc.healthper.repository.post;

import umc.healthper.domain.post.Post;
import umc.healthper.dto.post.PostSortingCriteria;

import java.util.List;

public interface PostRepositoryCustom {

    /**
     * 전달받은 정렬 기준으로 정렬된 게시글 목록을 불러온다.
     * - Paging: 30개
     * - 정렬 기준: LATEST(최신순), LIKE(추천순), COMMENT(댓글순)
     *
     * @param sortingCriteria
     * @param page
     * @return Post 객체가 담긴 List return
     */
    List<Post> findPostList(PostSortingCriteria sortingCriteria, Integer page);

    /**
     * 전달받은 post 객체를 삭제된 상태(postStatus=REMOVED)로 변경
     *
     * @param post
     */
    void removePost(Post post);
}
