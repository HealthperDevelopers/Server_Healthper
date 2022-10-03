package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.report.ReportedCountResponseDto;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.report.ReportService;

@Tag(name = "Report", description = "신고 API")
@RequestMapping("/report")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "게시글 신고",
            description = "`postId`에 해당하는 게시글을 신고합니다. 동일한 게시글은 한번만 신고 가능합니다.")
    @PostMapping("/post/{postId}")
    public void reportPost(@Parameter(hidden = true) @Login Long loginMemberId,
                           @PathVariable Long postId) {
        reportService.reportPost(loginMemberId, postId);
    }

    @Operation(summary = "게시글 신고 횟수 조회",
            description = "`postid`에 해당하는 게시글의 누적 신고 횟수를 조회합니다.")
    @GetMapping("/post/{postId}")
    public ReportedCountResponseDto getPostReportedCount(@PathVariable Long postId) {
        return new ReportedCountResponseDto(reportService.getPostReportedCount(postId));
    }

    @Operation(summary = "댓글 신고",
            description = "`postId`에 해당하는 댓글을 신고합니다. 동일한 댓글은 한번만 신고 가능합니다.")
    @PostMapping("/comment/{commentId}")
    public void reportComment(@Parameter(hidden = true) @Login Long loginMemberId,
                              @PathVariable Long commentId) {
        reportService.reportComment(loginMemberId, commentId);
    }

    @Operation(summary = "댓글 신고 횟수 조회",
            description = "`commentId에 해당하는 댓글의 누적 신고 횟수를 조회합니다.")
    @GetMapping("/comment/{commentId}")
    public ReportedCountResponseDto getCommentReportedCount(@PathVariable Long commentId) {
        return new ReportedCountResponseDto(reportService.getCommentReportedCount(commentId));
    }
}
