package umc.healthper.dto.comment;

import lombok.Data;
import umc.healthper.domain.Comment;
import umc.healthper.domain.CommentStatus;
import umc.healthper.domain.Member;
import umc.healthper.dto.member.MemberInfoDto;

import java.time.LocalDateTime;

@Data
public class NestedCommentResponseDto {

    private Long commentId;
    private MemberInfoDto writer;
    private String content;
    private CommentStatus status;
    private LocalDateTime createdAt;

    public NestedCommentResponseDto(Comment child) {
        Member writer = child.getMember();
        this.setWriter(new MemberInfoDto(writer.getId(), writer.getNickname(), writer.getStatus()));
        this.setCommentId(child.getId());
        this.setContent(child.getContent());
        this.setStatus(child.getStatus());
        this.setCreatedAt(child.getCreatedAt());
    }
}
