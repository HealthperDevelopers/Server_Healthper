package umc.healthper.dto.post;

import lombok.*;
import umc.healthper.domain.post.PostType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatePostRequestDto {
    private PostType type;
    private String title;
    private String content;
}
