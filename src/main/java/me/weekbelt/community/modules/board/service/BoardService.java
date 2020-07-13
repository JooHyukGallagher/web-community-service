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
        switch (boardType) {
            case "notice":
                boardList = boardRepository.findAllByBoardType(BoardType.NOTICE, pageable);
                break;
            case "free":
                boardList = boardRepository.findAllByBoardType(BoardType.FREE, pageable);
                break;
            case "question":
                boardList = boardRepository.findAllByBoardType(BoardType.QUESTION, pageable);
                break;
            case "promotion":
                boardList = boardRepository.findAllByBoardType(BoardType.PROMOTION, pageable);
                break;
            default:
                boardList = boardRepository.findAll(pageable);
                break;
        }

        return boardList.map(board ->
                BoardListElementForm.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .nickname(board.getAccount().getNickname())
                        .createdDateTime(board.getCreatedDateTime())
                        .viewCount(board.getViewCount())
                        .build()
        );
    }

    public BoardReadForm findBoardReadFormById(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다. id=" + id));
        board.plusViewCount();
        return BoardReadForm.builder()
                .id(board.getId())
                .boardType(board.getBoardType())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDateTime(board.getCreatedDateTime())
                .viewCount(board.getViewCount())
                .nickname(board.getAccount().getNickname())
                .build();
    }

    public void createBoard(Account account, BoardWriteForm boardWriteForm) {
        Account findAccount = accountRepository.findById(account.getId()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 계정입니다."));
        Board board = BoardDtoFactory.boardWriteFormToBoard(findAccount, boardWriteForm);
        boardRepository.save(board);
    }
}
