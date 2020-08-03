package me.weekbelt.community.modules.reply.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.reply.form.ReplyCreateForm;
import me.weekbelt.community.modules.reply.form.ReplyList;
import me.weekbelt.community.modules.reply.form.ReplyReadForm;
import me.weekbelt.community.modules.reply.form.ReplyUpdateForm;
import me.weekbelt.community.modules.reply.service.ReplyService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/replies")
@RestController
public class ReplyController {

    private final ReplyService replyService;

    // TODO: 댓글 도배기능
    @PostMapping
    public ResponseEntity<?> createReply(@CurrentAccount Account account,
                                         @RequestBody @Valid ReplyCreateForm replyCreateForm,
                                         @PathVariable("boardId") Long boardId,
                                         Errors errors) {

        if (errors.hasErrors()) {
            // TODO: 정확한 에러메시지 전달
            return ResponseEntity.badRequest().build();
        }

        ReplyReadForm replyReadForm = replyService.createReply(replyCreateForm, account.getNickname(), boardId);
        return ResponseEntity.ok(replyReadForm);
    }

    @GetMapping
    public ResponseEntity<?> replyList(@CurrentAccount Account account, @PathVariable Long boardId,
                                       @PageableDefault(size = 10, sort = "id",
                                               direction = Sort.Direction.DESC) Pageable pageable) {

        ReplyList replyList = replyService.getReplyList(boardId, pageable);
        return ResponseEntity.ok(replyList);
    }

    @PutMapping("{replyId}")
    public ResponseEntity<?> modifyReply(@CurrentAccount Account account, @PathVariable Long replyId,
                                         @RequestBody @Valid ReplyUpdateForm replyUpdateForm,
                                         Errors errors) {
        if (errors.hasErrors()) {
            // TODO: 정확한 에러메시지 전달
            return ResponseEntity.badRequest().build();
        }

        ReplyReadForm replyReadForm = replyService.modifyReply(replyUpdateForm, replyId);
        return ResponseEntity.ok(replyReadForm);
    }

    @DeleteMapping("{replyId}")
    public ResponseEntity<?> removeReply(@CurrentAccount Account account, @PathVariable Long replyId) {
        ReplyReadForm replyReadForm = replyService.removeReply(replyId);
        return ResponseEntity.ok(replyReadForm);
    }
}
