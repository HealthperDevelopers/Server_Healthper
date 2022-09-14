package umc.healthper.dto.post;

import lombok.*;
import umc.healthper.domain.comment.CommentType;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.*;
import umc.healthper.dto.comment.CommentResponseDtoWithChildren;
import umc.healthper.dto.member.MemberInfoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long postId;
    private PostType postType;
    private MemberInfoDto writer;
    private String title;
    private String content;
    private Integer likeCount;
    private PostStatus status;
    private LocalDateTime createdAt;
    private List<CommentResponseDtoWithChildren> comments = new ArrayList<>();
    // 이미지 파일

    /**
     * @param post        DTO로 변환할 Post 객체
     * @param loginMember 차단된 댓글 존재 여부를 판단해야 하기 때문에 로그인 사용자도 함께 전달받는다.
     */
    public PostResponseDto(Post post, Member loginMember) {
        this.setPostType(PostType.getPostType(post));

        Member writer = post.getMember();
        this.setWriter(new MemberInfoDto(writer.getId(), writer.getNickname(), writer.getStatus()));

        this.setLikeCount(post.getPostLikeCount());
        this.setPostId(post.getId());
        this.setTitle(post.getTitle());
        this.setContent(post.getContent());
        this.setStatus(post.getStatus());
        this.setCreatedAt(post.getCreatedAt());
        post.getComments().stream()
                .filter(comment -> comment.getCommentType() == CommentType.COMMENT)
                .forEachOrdered(comment -> {
                    boolean isBlocked = isBlockedComment(loginMember, comment.getMember());
                    this.getComments().add(new CommentResponseDtoWithChildren(comment, loginMember, isBlocked));
                });
    }

    /**
     * 특정 댓글에 대해 내가 차단한 사용자가 작성한 댓글인지 판별
     *
     * @param loginMember   로그인 사용자
     * @param commentWriter 댓글 작성자
     * @return 내가 차단한 사용자가 작성한 댓글일 경우 true return, 아닌 경우에는 false return
     */
    private boolean isBlockedComment(Member loginMember, Member commentWriter) {
        return loginMember.getMemberBlocks().stream()
                .anyMatch(memberBlock -> memberBlock.getBlockedMember() == commentWriter);
    }
}
