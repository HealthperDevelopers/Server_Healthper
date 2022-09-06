package umc.healthper.dto.post;

import lombok.Data;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostType;
import umc.healthper.dto.member.MemberInfoDto;

import java.time.LocalDateTime;

@Data
public class ListPostResponseDto {

    private Long postId;
    private PostType postType;
    private MemberInfoDto writer;
    private String title;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;

    public ListPostResponseDto(Post post) {
        this.setPostType(PostType.getPostType(post));

        Member writer = post.getMember();
        this.setWriter(new MemberInfoDto(writer.getId(), writer.getNickname(), writer.getStatus()));

        this.setPostId(post.getId());
        this.setTitle(post.getTitle());
        this.setCommentCount(post.getComments().size());
        this.setLikeCount(post.getPostLikeCount());
        this.setCreatedAt(post.getCreatedAt());
    }
}
