package umc.healthper.repository.comment;

import lombok.RequiredArgsConstructor;
import umc.healthper.domain.comment.Comment;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final EntityManager em;

    @Override
    public void removeComment(Comment comment) {
        comment.delete();
    }
}
