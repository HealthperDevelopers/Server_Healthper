package umc.healthper.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.domain.comment.CommentType;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostStatus;
import umc.healthper.dto.comment.CommentResponseDto;
import umc.healthper.dto.member.MemberInfoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Post 객체를 Parameter로 전달받아서 응답하기 위한 Format으로 변환
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long postId;
    private MemberInfoDto writer;
    private String title;
    private String content;
    private PostStatus postStatus;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments = new ArrayList<>();
    // 좋아요 개수
    // 이미지 파일

    public PostResponseDto(Post post) {
        Member writer = post.getMember();
        this.setWriter(new MemberInfoDto(writer.getId(), writer.getNickname(), writer.getStatus()));
        this.setPostId(post.getId());
        this.setTitle(post.getTitle());
        this.setContent(post.getContent());
        this.setPostStatus(post.getStatus());
        this.setCreatedAt(post.getCreatedAt());
        post.getComments().stream()
                .filter(comment -> comment.getCommentType() == CommentType.COMMENT)
                .forEach(comment -> this.getComments().add(new CommentResponseDto(comment)));
    }
}
