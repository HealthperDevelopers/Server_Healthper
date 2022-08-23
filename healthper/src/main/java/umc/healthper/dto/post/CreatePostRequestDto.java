package umc.healthper.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePostRequestDto {
    private String postType;
    private String title;
    private String content;
}
