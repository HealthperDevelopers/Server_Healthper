package umc.healthper.dto.comment;

import lombok.Data;

@Data
public class CreateNestedCommentRequestDto {
    private Long postId;
    private Long parentId;
    private String content;
}
