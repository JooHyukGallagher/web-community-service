package me.weekbelt.community.modules.reply.service;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import me.weekbelt.community.modules.reply.Reply;
import me.weekbelt.community.modules.reply.ReplyDtoFactory;
import me.weekbelt.community.modules.reply.form.ReplyCreateForm;
import me.weekbelt.community.modules.reply.form.ReplyList;
import me.weekbelt.community.modules.reply.form.ReplyReadForm;
import me.weekbelt.community.modules.reply.form.ReplyUpdateForm;
import me.weekbelt.community.modules.reply.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    public ReplyReadForm createReply(ReplyCreateForm replyCreateForm, String nickname, Long boardId) {
        Account account = accountRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 계정이 존재하지 않습니다. nickname=" + nickname));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. boardId=" + boardId));

        Reply reply = ReplyDtoFactory.replyCreateFormToReply(account, board, replyCreateForm);
        replyRepository.save(reply);

        return ReplyDtoFactory.replyToReplyReadForm(reply);
    }

    public ReplyList getReplyList(Long boardId, Pageable pageable) {
        Page<Reply> replyList = replyRepository.findByBoardId(boardId, pageable);
        Page<ReplyReadForm> replyReadList = ReplyDtoFactory.replyPageToReplyReadFormPage(replyList);

        return ReplyList.builder()
                .replyList(replyReadList)
                .replyNum(replyReadList.getTotalElements())
                .build();
    }

    public void modifyReply(ReplyUpdateForm replyUpdateForm, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. replyId=" + replyId));

        reply.update(replyUpdateForm);
    }

    public void removeReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. replyId=" + replyId));
        replyRepository.delete(reply);
    }
}
