package me.weekbelt.community.modules.reply;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.reply.form.ReplyCreateForm;
import me.weekbelt.community.modules.reply.form.ReplyReadForm;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class ReplyDtoFactory {
    public static Reply replyCreateFormToReply(Account account, Board board, ReplyCreateForm replyCreateForm) {
        return Reply.builder()
                .account(account)
                .board(board)
                .content(replyCreateForm.getContent())
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
    }

    public static ReplyReadForm replyToReplyReadForm(Reply reply) {
        return ReplyReadForm.builder()
                .id(reply.getId())
                .boardId(reply.getBoard().getId())
                .nickname(reply.getAccount().getNickname())
                .content(reply.getContent())
                .createdDateTime(reply.getCreatedDateTime())
                .modifiedDateTime(reply.getModifiedDateTime())
                .build();
    }

    public static Page<ReplyReadForm> replyPageToReplyReadFormPage(Page<Reply> replyList) {
        return replyList.map(ReplyDtoFactory::replyToReplyReadForm);
    }
}
