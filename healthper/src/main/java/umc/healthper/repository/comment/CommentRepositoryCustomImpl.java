package umc.healthper.repository.comment;

import lombok.RequiredArgsConstructor;
import umc.healthper.domain.comment.Comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    @Override
    public void removeComment(Comment comment) {
        comment.delete();
    }
}
