package umc.healthper.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
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
    private String nickname;

    @NotNull
    private Integer reportedCount;

    @NotNull
    @Enumerated(EnumType.STRING)
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
        return new Member(kakaoKey, nickname, 0, MemberStatus.NORMAL);
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
    private Member(Long kakaoKey, String nickname, Integer reportedCount, MemberStatus status) {
        this.kakaoKey = kakaoKey;
        this.nickname = nickname;
        this.reportedCount = reportedCount;
        this.status = status;
    }

    //== Setter ==//
    private void setStatus(MemberStatus status) {
        this.status = status;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
