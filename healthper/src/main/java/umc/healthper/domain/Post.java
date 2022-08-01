package umc.healthper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    //== Constructor ==//
    public Post(Member member, String title, String content) {
        this(member, title, content, NORMAL);
    }

    public Post(Member member, String title, String content, PostStatus postStatus) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.postStatus = postStatus;
    }

    //== Setter ==//
    private void setId(Long id) {
        this.id = id;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    private void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    //== 수정 메서드 ==//
    public void change(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
    }

    public void delete() {
        this.setPostStatus(REMOVED);
    }
}
