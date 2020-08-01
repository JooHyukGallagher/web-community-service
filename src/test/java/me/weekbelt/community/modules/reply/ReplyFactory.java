package me.weekbelt.community.modules.reply;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.reply.form.ReplyCreateForm;
import me.weekbelt.community.modules.reply.repository.ReplyRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReplyFactory {

    private final ReplyRepository replyRepository;

    public Reply createReply(Account account, Board board, ReplyCreateForm replyCreateForm) {
        Reply reply = Reply.builder()
                .account(account)
                .board(board)
                .content(replyCreateForm.getContent())
                .createdDateTime(replyCreateForm.getCreatedDateTime())
                .modifiedDateTime(replyCreateForm.getModifiedDateTime())
                .build();

        return replyRepository.save(reply);
    }
}
