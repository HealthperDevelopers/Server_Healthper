package umc.healthper.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.domain.Post;
import umc.healthper.domain.PostStatus;
import umc.healthper.dto.member.MemberInfoDto;

/**
 * Post 객체를 parameter로 전달받아서 응답하기 위한 Format으로 변환
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private MemberInfoDto writer;
    private Long postId;
    private String title;
    private String content;
    private PostStatus postStatus;
    // 좋아요 개수
    // 이미지 파일

    public PostResponseDto(Post post) {
        this.setWriter(new MemberInfoDto(post.getMember().getId(), post.getMember().getNickname(), post.getMember().getStatus()));
        this.setPostId(post.getId());
        this.setTitle(post.getTitle());
        this.setContent(post.getContent());
        this.setPostStatus(post.getPostStatus());
    }
}
