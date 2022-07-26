package umc.healthper.domain;

import lombok.Getter;
import lombok.Setter;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

//    @OneToMany(mappedBy = "parent")
//    private List<Comment> child = new ArrayList<>();

    //== 연관관계 편의 Method ==//
//    public void addChildComment(Comment child) {
//        this.child.add(child);
//        child.setParent(this);
//    }
}
