package umc.healthper.repository.comment;

import umc.healthper.domain.comment.Comment;

public interface CommentRepositoryCustom {

    /**
     * 전달받은 comment 객체를 삭제된 상태(commentStatus=REMOVED)로 변경
     * @param comment
     */
    void removeComment(Comment comment);
}
