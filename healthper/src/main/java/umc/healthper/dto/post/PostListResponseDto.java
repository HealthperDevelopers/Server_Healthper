package umc.healthper.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostListResponseDto {

    private List<ListPostResponseDto> content = new ArrayList<>();
}
