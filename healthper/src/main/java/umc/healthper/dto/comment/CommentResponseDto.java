package umc.healthper.dto.comment;

import lombok.Getter;
import lombok.Setter;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.comment.CommentStatus;
import umc.healthper.domain.member.Member;
import umc.healthper.dto.member.MemberInfoDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long commentId;
    private MemberInfoDto writer;
    private boolean block;
    private String content;
    private Integer likeCount;
    private CommentStatus status;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment, boolean block) {
        this.setCommentId(comment.getId());

        Member writer = comment.getMember();
        this.setWriter(new MemberInfoDto(writer.getId(), writer.getNickname(), writer.getStatus()));

        this.setBlock(block);
        this.setContent(comment.getContent());
        this.setLikeCount(comment.getLikes().size());
        this.setStatus(comment.getStatus());
        this.setCreatedAt(comment.getCreatedAt());
    }
}
