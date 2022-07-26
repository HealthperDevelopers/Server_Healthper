package umc.healthper.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    //== Constructor ==//
    public Post(String title, String content, PostStatus postStatus) {
        this.setTitle(title);
        this.setContent(content);
        this.setPostStatus(postStatus);
    }

    //== 수정 메서드 ==//
    public void change(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
    }
}
