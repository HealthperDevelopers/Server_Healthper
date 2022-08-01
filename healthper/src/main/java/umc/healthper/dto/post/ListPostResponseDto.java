package umc.healthper.dto.post;

import lombok.Data;
import umc.healthper.domain.Post;
import umc.healthper.dto.member.MemberInfoDto;

import java.time.LocalDateTime;

@Data
public class ListPostResponseDto {

    private MemberInfoDto writer;
    private Long postId;
    private String title;
    private LocalDateTime createdAt;

    public ListPostResponseDto(Post post) {
        this.setWriter(new MemberInfoDto(post.getMember().getId(), post.getMember().getNickname(), post.getMember().getStatus()));
        this.setPostId(post.getId());
        this.setTitle(post.getTitle());
        this.setCreatedAt(post.getCreatedAt());
    }
}
