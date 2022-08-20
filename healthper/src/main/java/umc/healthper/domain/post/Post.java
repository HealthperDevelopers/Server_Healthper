package umc.healthper.domain.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.like.PostLike;
import umc.healthper.domain.member.Member;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static umc.healthper.domain.post.PostStatus.*;

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
    private Integer viewCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostStatus status;  // NORMAL, REMOVED, BLOCKED

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL)
    private Set<PostLike> likes = new HashSet<>();

    //== 생성 메서드 ==//
    public static Post createPost(Member member, String title, String content) {
        return new Post(member, title, content, 0, NORMAL);
    }

    //== 수정 메서드 ==//
    public void addViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
    }

    public void delete() {
        this.setStatus(REMOVED);
    }

    //== Constructor ==//
    private Post(Member member, String title, String content, int viewCount, PostStatus postStatus) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.status = postStatus;
    }

    //== Setter ==//
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