package umc.healthper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static umc.healthper.domain.MemberStatus.*;

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

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Member createMember(Long kakaoKey, String nickname) {
        return new Member(kakaoKey, nickname, 0, NORMAL);
    }

    //== Constructor ==//
    private Member(Long kakaoKey, String nickname, Integer reportedCount, MemberStatus status) {
        this.kakaoKey = kakaoKey;
        this.nickname = nickname;
        this.reportedCount = reportedCount;
        this.status = status;
    }

    //== Setter ==//
    private void setId(Long id) {
        this.id = id;
    }

    private void setKakaoKey(Long kakaoKey) {
        this.kakaoKey = kakaoKey;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setReportedCount(Integer reportedCount) {
        this.reportedCount = reportedCount;
    }

    private void setStatus(MemberStatus status) {
        this.status = status;
    }

    private void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    private void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
