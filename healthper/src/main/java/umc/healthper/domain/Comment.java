package umc.healthper.domain;

import lombok.Getter;

//import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
@Getter
public class Comment {

//    @Id
//    @GeneratedValue
//    @Column(name = "comment_id")
    private int id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
    private Post post;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime createdDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id")
    private Comment parent;

//    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    private void setParent(Comment parent) {
        this.parent = parent;
    }

    //== 연관관계 편의 Method ==//
    public void addChildComment(Comment child) {
        this.child.add(child);
        child.setParent(this);
    }
}
