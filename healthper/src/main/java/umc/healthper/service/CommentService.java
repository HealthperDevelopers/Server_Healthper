package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.Comment;
import umc.healthper.repository.CommentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Comment 등록(저장)
     */
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
