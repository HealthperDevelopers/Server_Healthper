package umc.healthper.domain.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PostLike> likes = new HashSet<>();

    @Formula("(select count(1) " +
            "from comment c " +
            "where c.post_id=post_id)")
    private int commentCount;

    @Formula("(select count(1) " +
            "from comment c " +
            "where c.post_id=post_id and c.status='NORMAL')")
    private int commentCountOnlyNormalStatus;

    @Formula("(select count(1) " +
            "from post_like pl " +
            "where pl.post_id=post_id)")
    private int postLikeCount;

    //== 생성 메서드 ==//
    public static Post createPost(Member member, PostType postType, String title, String content) {
        switch (postType) {
            case NORMAL:
                return new NormalPost(member, title, content, 0, PostStatus.NORMAL);
            case QUESTION:
                return new QuestionPost(member, title, content, 0, PostStatus.NORMAL);
            case AUDIO:
                return new AudioPost(member, title, content, 0, PostStatus.NORMAL);
            default:
                throw new IllegalArgumentException("잘못된 PostType");
        }
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
        this.setStatus(PostStatus.REMOVED);
    }

    //== Constructor ==//
    protected Post(Member member, String title, String content, int viewCount, PostStatus postStatus) {
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
