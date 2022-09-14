package umc.healthper.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.domain.post.PostType;

@Data
@NoArgsConstructor
public class CreatePostRequestDto {
    private PostType type;
    private String title;
    private String content;
}
