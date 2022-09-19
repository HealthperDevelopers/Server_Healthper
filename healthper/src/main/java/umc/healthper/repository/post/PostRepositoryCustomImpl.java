package umc.healthper.repository.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.post.PostType;
import umc.healthper.dto.post.PostSortingCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final static int NUMBER_OF_PAGING = 30;
    private final EntityManager em;

    @Override
    public List<Post> findPosts(PostType postType, PostSortingCriteria sort,
                                Integer page, Member loginMember) {

        // Sorting
        String sortingQuery = "order by ";

        switch (sort) {
            case LATEST:
                break;
            case LIKE:
                sortingQuery += "p.postLikeCount desc, ";
                break;
            case COMMENT:
                sortingQuery += "p.commentCount desc, ";
                break;
            default:
                throw new IllegalArgumentException("잘못된 정렬 기준입니다.");
        }
        sortingQuery += "p.createdAt desc";

        // Filtering
        String filteringQuery = "where ";

        // 조회하고자 하는 게시글 종류만 Fetch
        switch (postType) {
            case NORMAL:
                filteringQuery += "type(p)=NormalPost ";
                break;
            case QUESTION:
                filteringQuery += "type(p)=QuestionPost ";
                break;
            case AUDIO:
                filteringQuery += "type(p)=AudioPost ";
                break;
            default:
                throw new IllegalArgumentException("잘못된 게시글 종류입니다.");
        }

        // 삭제된 게시글, 신고당해 차단된 게시글 제외
        filteringQuery += "and p.status<>'REMOVED' and p.status<>'BLOCKED' ";

        // 내가 차단한 회원의 게시글 제외
        List<Long> blockedMemberIds = loginMember.getMemberBlocks().stream()
                .map(memberBlock -> memberBlock.getBlockedMember().getId())
                .collect(Collectors.toList());
        boolean existsBlockedMembers = !blockedMemberIds.isEmpty();
        if (existsBlockedMembers) {
            filteringQuery += "and p.member.id not in :blockedMemberIds ";
        }


        TypedQuery<Post> emQuery = em.createQuery("select p from Post p " + filteringQuery + sortingQuery, Post.class);
        if (existsBlockedMembers) {
            emQuery.setParameter("blockedMemberIds", blockedMemberIds);
        }
        return emQuery.setFirstResult(page * NUMBER_OF_PAGING)
                .setMaxResults(NUMBER_OF_PAGING)
                .getResultList();
    }

    @Override
    public void removePost(Post post) {
        post.delete();
    }
}
