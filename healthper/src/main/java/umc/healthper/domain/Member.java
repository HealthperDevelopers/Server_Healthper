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
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private Long kakaoKey;

    private String nickName;

    private Integer warnCount;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();
}
