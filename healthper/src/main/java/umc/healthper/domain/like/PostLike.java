package umc.healthper.domain.like;

import lombok.*;
import umc.healthper.domain.Member;
import umc.healthper.domain.Post;
import umc.healthper.global.BaseTimeEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //== 생성 메서드 ==//
    public static PostLike createPostLike(Member member, Post post) {
        PostLike postLike = new PostLike();
        postLike.setMember(member);
        postLike.setPost(post);
        return postLike;
    }

    //== 연관관계 편의 메서드 ==//
    private void setMember(Member member) {
        this.member = member;
        member.getPostLikes().add(this);
    }

    private void setPost(Post post) {
        this.post = post;
        post.getLikes().add(this);
    }
}
