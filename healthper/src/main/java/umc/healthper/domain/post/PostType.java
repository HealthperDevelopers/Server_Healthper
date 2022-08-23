package umc.healthper.domain.post;

import java.util.Arrays;

public enum PostType {
    NORMAL("NORMAL"),
    QUESTION("QUESTION"),
    AUDIO("AUDIO");

    private final String postTypeKey;

    PostType(String postTypeKey) {
        this.postTypeKey = postTypeKey;
    }

    /**
     * 문자열을 전달받아서 해당하는 PostType enum value를 반환해주는 메서드
     *
     * @param postTypeKey - Normal, Question, Audio
     * @return 전달받은 문자열에 해당하는 PostType enum value return
     */
    public static PostType transferFromString(String postTypeKey) {
        return Arrays.stream(PostType.values())
                .filter(postType -> postType.postTypeKey.equalsIgnoreCase(postTypeKey))
                .findFirst()
                .orElse(null);  // Exception Throw 하도록 변경 필요
    }

    /**
     * Post 객체를 전달받아서 일치하는 PostType enum value를 반환해주는 메서드
     * @param post
     * @return 전달받은 Post 객체에 해당하는 PostType enum value return
     */
    public static PostType getPostType(Post post) {
        if (post instanceof NormalPost) {
            return PostType.NORMAL;
        } else if (post instanceof QuestionPost) {
            return PostType.QUESTION;
        } else if (post instanceof AudioPost) {
            return PostType.AUDIO;
        } else {
            return null;
        }
    }
}
