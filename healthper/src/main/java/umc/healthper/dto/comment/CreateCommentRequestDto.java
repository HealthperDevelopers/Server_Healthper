package umc.healthper.dto.comment;

import lombok.Data;

@Data
public class CreateCommentRequestDto {
    private Long memberId;
    private Long postId;
    private String content;
}
