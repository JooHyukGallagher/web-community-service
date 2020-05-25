package me.weekbelt.community.modules.board.service;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardDtoFactory;
import me.weekbelt.community.modules.board.form.BoardRequestForm;
import me.weekbelt.community.modules.board.form.BoardResponseForm;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    public BoardResponseForm createBoard(Account account, BoardRequestForm boardRequestForm){
        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 계정입니다."));

        Board board = BoardDtoFactory.boardRequestFormToBoard(findAccount, boardRequestForm);
        Board savedBoard = boardRepository.save(board);
        return BoardDtoFactory.boardToBoardResponseForm(savedBoard);
    }
}
