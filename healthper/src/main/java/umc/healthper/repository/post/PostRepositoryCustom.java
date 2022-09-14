package umc.healthper.repository.post;

import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.dto.post.PostSortingCriteria;

import java.util.List;

public interface PostRepositoryCustom {

    /**
     * 전달받은 정렬 기준으로 정렬된 게시글 목록을 불러온다.
     * - Paging: 30개
     * - 정렬 기준: LATEST(최신순), LIKE(추천순), COMMENT(댓글순)
     *
     * @param sort 정렬 기준
     * @param page 페이지 번호
     * @param loginMember 로그인 멤버
     * @return 조회한 Post List
     */
    List<Post> findPosts(PostSortingCriteria sort, Integer page, Member loginMember);

    /**
     * 전달받은 post 객체를 삭제된 상태(status=REMOVED)로 변경
     *
     * @param post 삭제할 Post 객체
     */
    void removePost(Post post);
}
