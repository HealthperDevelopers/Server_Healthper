package umc.healthper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static umc.healthper.domain.PostStatus.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostStatus status;  // NORMAL, REMOVED, BLOCKED

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Post createPost(Member member, String title, String content) {
        return new Post(member, title, content, NORMAL);
    }

    //== 수정 메서드 ==//
    public void change(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
    }

    public void delete() {
        this.setStatus(REMOVED);
    }

    //== Constructor ==//
    private Post(Member member, String title, String content, PostStatus postStatus) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = postStatus;
    }

    //== Setter ==//
    private void setMember(Member member) {
        this.member = member;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setStatus(PostStatus postStatus) {
        this.status = postStatus;
    }
}
