package umc.healthper.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.domain.Comment;
import umc.healthper.domain.CommentStatus;
import umc.healthper.domain.Member;
import umc.healthper.dto.member.MemberInfoDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private MemberInfoDto writer;
    private String content;
    private CommentStatus status;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        Member writer = comment.getMember();
        this.setWriter(new MemberInfoDto(writer.getId(), writer.getNickname(), writer.getStatus()));
        this.setCommentId(comment.getId());
        this.setContent(comment.getContent());
        this.setStatus(comment.getStatus());
        this.setCreatedAt(comment.getCreatedAt());
    }
}
