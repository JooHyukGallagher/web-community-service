package me.weekbelt.community.modules.reply.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.reply.form.ReplyCreateForm;
import me.weekbelt.community.modules.reply.form.ReplyReadForm;
import me.weekbelt.community.modules.reply.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/replies")
@RestController
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<?> createReply(@CurrentAccount Account account,
                                         @RequestBody @Valid ReplyCreateForm replyCreateForm,
                                         @PathVariable("boardId") Long boardId,
                                         Errors errors) {

        if(errors.hasErrors()) {
            // TODO: 정확한 에러메시지 전달
            return ResponseEntity.badRequest().build();
        }

        ReplyReadForm replyReadForm = replyService.createReply(replyCreateForm, account.getNickname(), boardId);
        return ResponseEntity.ok(replyReadForm);
    }
}
