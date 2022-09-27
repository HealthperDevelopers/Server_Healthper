package umc.healthper.domain.report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "POST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_id")
    private Post reportedPost;

    //== Constructor ==//
    public PostReport(Member member, Post reportedPost) {
        super(member);
        this.reportedPost = reportedPost;
    }

    //== Create Method ==//
    public static PostReport createPostReport(Member member, Post post) {
        return new PostReport(member, post);
    }
}