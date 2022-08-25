package umc.healthper.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostSliceResponseDto {

    private List<ListPostResponseDto> content = new ArrayList<>();  // 조회한 데이터
    private PostSortingCriteria sort;   // 정렬 기준
    private int pageNum;            // 현재 페이지 번호
    private int size;               // 한 페이지에 담기는 데이터의 최대 개수
    private int numberOfElements;   // 현재 페이지에 담긴 데이터의 개수
    private boolean first;        // 첫 페이지인가?
    private boolean last;         // 마지막 페이지인가?
    private boolean hasNext;        // 다음 페이지가 존재하는지에 대한 여부

    public PostSliceResponseDto(Slice slice, PostSortingCriteria sort) {
        this.content = slice.getContent();
        this.sort = sort;
        this.pageNum = slice.getNumber();
        this.size = slice.getSize();
        this.numberOfElements = slice.getNumberOfElements();
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.hasNext = slice.hasNext();
    }
}
