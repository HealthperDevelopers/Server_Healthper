package umc.healthper.dto.comment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateNestedCommentRequestDto {
    private Long postId;
    private Long parentId;
    private String content;
}
