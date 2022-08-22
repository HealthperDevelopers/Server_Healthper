package umc.healthper.dto.comment;

import lombok.Data;

@Data
public class CreateCommentRequestDto {
    private Long postId;
    private Long parentId;
    private String content;
}
