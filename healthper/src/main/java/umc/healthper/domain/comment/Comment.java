package umc.healthper.domain.comment;

import lombok.Getter;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.*;
import static umc.healthper.domain.comment.CommentStatus.*;
import static umc.healthper.domain.comment.CommentType.*;

@Entity
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String Content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommentType commentType;    // COMMENT, NESTED

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommentStatus status;    // NORMAL, REMOVED, BLOCKED

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<CommentLike> likes = new HashSet<>();

    //== 생성 메서드 ==//
    public static Comment createComment(Member member, Post post, String content) {
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCommentType(COMMENT);
        comment.setStatus(NORMAL);
        return comment;
    }

    //== 수정 메서드 ==//
    public void update(String content) {
        this.setContent(content);
    }

    public void delete() {
        this.setStatus(REMOVED);
    }

    //== 연관관계 편의 Method ==//
    private void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void addChildComment(Comment child) {
        this.getChildren().add(child);
        child.setParent(this);
        child.setCommentType(NESTED);
    }

    //== Setter ==//
    private void setMember(Member member) {
        this.member = member;
    }

    private void setContent(String content) {
        Content = content;
    }

    private void setCommentType(CommentType commentType) {
        this.commentType = commentType;
    }

    private void setStatus(CommentStatus status) {
        this.status = status;
    }

    private void setParent(Comment parent) {
        this.parent = parent;
    }
}
