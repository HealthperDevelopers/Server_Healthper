package umc.healthper.domain.post;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum PostType {
    NORMAL, QUESTION, AUDIO;

    /**
     * Post 객체를 전달받아서 일치하는 PostType enum value를 반환해주는 메서드
     *
     * @param post PostType을 확인할 Post 객체
     * @return 전달받은 Post 객체에 해당하는 PostType enum value return
     * @throws IllegalArgumentException 전달받은 Post 객체의 PostType에 문제가 있는 경우
     */
    public static PostType getPostType(Post post) {
        if (post instanceof NormalPost) {
            return PostType.NORMAL;
        } else if (post instanceof QuestionPost) {
            return PostType.QUESTION;
        } else if (post instanceof AudioPost) {
            return PostType.AUDIO;
        } else {
            log.error("Post 객체의 postType을 가져오던 중 문제가 발생했습니다.");
            throw new IllegalArgumentException();
        }
    }
}
