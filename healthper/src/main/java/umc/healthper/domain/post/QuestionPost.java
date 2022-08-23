package umc.healthper.domain.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.healthper.domain.member.Member;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("QUESTION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionPost extends Post {

    public QuestionPost(Member member, String title, String content, int viewCount, PostStatus postStatus) {
        super(member, title, content, viewCount, postStatus);
    }
}
