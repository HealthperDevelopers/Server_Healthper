package umc.healthper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * How to create Post Object?
 * - Post.create(String title, String content)
 * - Post.create(Member member, String title, String content)
 */

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

//    private Integer likeCount;

    // Images

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Post create(String title, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setPostStatus(PostStatus.NORMAL);
        return post;
    }

    public static Post create(Member member, String title, String content) {
        Post post = new Post();
        post.setMember(member);
        post.setTitle(title);
        post.setContent(content);
        post.setPostStatus(PostStatus.NORMAL);
        return post;
    }

    //== 수정 메서드 ==//
    public void change(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
    }
}
