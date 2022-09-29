package umc.healthper.dto.comment;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateCommentRequestDto {
    private Long postId;
    private String content;
}
