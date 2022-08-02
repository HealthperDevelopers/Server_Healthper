package umc.healthper.repository.comment;

import lombok.RequiredArgsConstructor;
import umc.healthper.domain.Comment;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final EntityManager em;

    @Override
    public void removeComment(Long id) {
        Comment findComment = em.find(Comment.class, id);
        findComment.delete();
    }
}
