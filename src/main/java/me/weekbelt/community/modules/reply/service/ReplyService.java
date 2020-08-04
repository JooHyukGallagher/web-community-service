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

import javax.transaction.Transactional;


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

    public Page<ReplyReadForm> getReplyList(Long boardId, Pageable pageable) {
        Page<Reply> replyList = replyRepository.findByBoardIdOrderByIdDesc(boardId, pageable);

        return ReplyDtoFactory.replyPageToReplyReadFormPage(replyList);
    }

    public ReplyReadForm modifyReply(ReplyUpdateForm replyUpdateForm, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. replyId=" + replyId));

        reply.update(replyUpdateForm);

        return ReplyDtoFactory.replyToReplyReadForm(reply);
    }

    public ReplyReadForm removeReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. replyId=" + replyId));
        ReplyReadForm replyReadForm = ReplyDtoFactory.replyToReplyReadForm(reply);

        replyRepository.delete(reply);
        return replyReadForm;
    }
}
