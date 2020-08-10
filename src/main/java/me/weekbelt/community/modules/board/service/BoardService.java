package me.weekbelt.community.modules.board.service;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardDtoFactory;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.form.*;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    public Page<BoardListElementForm> findBoardList(String boardType, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize());

        Page<Board> boardList;
        boardList = getBoardPageByBoardType(boardType, pageable);

        return boardList.map(BoardDtoFactory::boardToBoardListElementForm);
    }

    private Page<Board> getBoardPageByBoardType(String boardType, Pageable pageable) {
        Page<Board> boardList;
        if (boardType.equals("ALL")) {
            boardList = boardRepository.findAllByOrderByIdDesc(pageable);
        } else {
            boardList = boardRepository.findAllByBoardTypeOrderByIdDesc(BoardType.valueOf(boardType), pageable);
        }
        return boardList;
    }

    public BoardReadForm findBoardReadFormById(Long id){
        Board board = boardRepository.findBoardWithAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 게시글이 없습니다. 요청한 게시글 ID=" + id));
        board.plusViewCount();
        return BoardDtoFactory.boardToBoardReadForm(board);
    }

    public void createBoard(Account account, BoardWriteForm boardWriteForm) {
        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 계정입니다. 요청한 계정 ID=" + account.getId()));
        Board board = BoardDtoFactory.boardWriteFormToBoard(findAccount, boardWriteForm);
        boardRepository.save(board);
    }

    public void updateBoard(Long boardId, BoardUpdateForm boardUpdateForm) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("찾는 게시글이 없습니다. 요청한 게시글 ID=" + boardId));
        board.update(boardUpdateForm);
    }

    public void removeBoard(Board board) {
        boardRepository.delete(board);
    }
}
