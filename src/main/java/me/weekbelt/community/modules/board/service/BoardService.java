package me.weekbelt.community.modules.board.service;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardDtoFactory;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.form.BoardListElementForm;
import me.weekbelt.community.modules.board.form.BoardWriteForm;
import me.weekbelt.community.modules.board.form.BoardReadForm;
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
        switch (boardType) {
            case "notice":
                boardList = boardRepository.findAllByBoardTypeOrderByIdDesc(BoardType.NOTICE, pageable);
                break;
            case "free":
                boardList = boardRepository.findAllByBoardTypeOrderByIdDesc(BoardType.FREE, pageable);
                break;
            case "question":
                boardList = boardRepository.findAllByBoardTypeOrderByIdDesc(BoardType.QUESTION, pageable);
                break;
            case "promotion":
                boardList = boardRepository.findAllByBoardTypeOrderByIdDesc(BoardType.PROMOTION, pageable);
                break;
            default:
                boardList = boardRepository.findAllByOrderByIdDesc(pageable);
                break;
        }
        return boardList;
    }

    public BoardReadForm findBoardReadFormById(Long id){
        Board board = boardRepository.findBoardWithAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다. id=" + id));
        board.plusViewCount();
        return BoardDtoFactory.boardToBoardReadForm(board);
    }

    public void createBoard(Account account, BoardWriteForm boardWriteForm) {
        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 계정입니다."));
        Board board = BoardDtoFactory.boardWriteFormToBoard(findAccount, boardWriteForm);
        boardRepository.save(board);
    }
}
