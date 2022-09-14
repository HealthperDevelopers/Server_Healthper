package umc.healthper.repository.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberBlock;
import umc.healthper.domain.post.Post;
import umc.healthper.dto.post.PostSortingCriteria;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final static int NUMBER_OF_PAGING = 30;
    private final EntityManager em;

    @Override
    public List<Post> findPosts(PostSortingCriteria sort, Integer page, Member loginMember) {

        // Sorting
        String sortingQuery = "";

        switch (sort) {
            case LATEST:
                sortingQuery += "order by p.createdAt desc";
                break;
            case LIKE:
                sortingQuery += "order by p.postLikeCount desc, p.createdAt desc";
                break;
            case COMMENT:
                sortingQuery += "order by p.commentCount desc, p.createdAt desc";
                break;
            default:
                throw new IllegalArgumentException("잘못된 정렬 기준입니다.");
        }

        // Filtering
        String filteringQuery = "where p.status<>'REMOVED' and p.status<>'BLOCKED' ";
        if (!loginMember.getMemberBlocks().isEmpty()) {
            filteringQuery += "and p.member not in (";
            Iterator<MemberBlock> iterator = loginMember.getMemberBlocks().iterator();
            filteringQuery += iterator.next().getBlockedMember().getId();
            while (iterator.hasNext()) {
                filteringQuery += ", " + iterator.next().getBlockedMember().getId();
            }
            filteringQuery += ") ";
        }

        return em.createQuery("select p from Post p " + filteringQuery + sortingQuery)
                .setFirstResult(page * NUMBER_OF_PAGING)
                .setMaxResults(NUMBER_OF_PAGING)
                .getResultList();
    }

    @Override
    public void removePost(Post post) {
        // 댓글들도 삭제해야 하는가?
        // -> 아니다. 댓글정보 클릭해서 게시글 조회하려고 할 때는 "삭제된 게시글입니다"만 띄워주면 될 것
        post.delete();
    }
}
