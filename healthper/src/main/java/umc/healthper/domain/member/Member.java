package umc.healthper.domain.member;

import lombok.*;
import umc.healthper.domain.block.MemberBlock;
import umc.healthper.domain.completeExercise.CompleteExercise;
import umc.healthper.domain.comment.CommentLike;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.RecordJPA;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.post.PostLike;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private Long kakaoKey;

    @NotNull
    @Setter(AccessLevel.PRIVATE)
    private String nickname;

    @NotNull
    private Integer reportedCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.PRIVATE)
    private MemberStatus status;    // NORMAL, RESIGNED, BLOCKED

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberBlock> memberBlocks = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<PostLike> postLikes = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<CommentLike> commentLikes = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private List<RecordJPA> records = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CompleteExercise> completeExercises = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Member createMember(Long kakaoKey, String nickname) {
        return Member.builder()
                .kakaoKey(kakaoKey)
                .nickname(nickname)
                .reportedCount(0)
                .status(MemberStatus.NORMAL)
                .build();
    }

    //== Business Logic ==//
    public void rejoin(String nickname) {
        this.setStatus(MemberStatus.NORMAL);
        this.setNickname(nickname);
    }

    public void update(String nickname) {
        this.setNickname(nickname);
    }

    public void delete() {
        this.setStatus(MemberStatus.RESIGNED);
    }

    //== Constructor ==//
    @Builder
    private Member(Long kakaoKey, String nickname, Integer reportedCount, MemberStatus status) {
        this.kakaoKey = kakaoKey;
        this.nickname = nickname;
        this.reportedCount = reportedCount;
        this.status = status;
    }
}
