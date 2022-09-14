package umc.healthper.dto.comment;

import lombok.*;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentResponseDtoWithChildren extends CommentResponseDto {

    private List<CommentResponseDto> children = new ArrayList<>();

    /**
     * @param comment     DTO로 변환할 Comment 객체
     * @param loginMember 대댓글들의 차단 여부를 판단해야 하기 때문에 로그인 사용자도 함께 전달받는다.
     * @param block       이 객체의 차단 여부
     */
    public CommentResponseDtoWithChildren(Comment comment, Member loginMember, boolean block) {
        super(comment, block);

        // 차단 여부를 확인하여 대댓글들을 DTO로 변환
        comment.getChildren().forEach(child -> {
            boolean isBlocked = isBlockedComment(loginMember, child.getMember());
            this.getChildren().add(new CommentResponseDto(child, isBlocked));
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
