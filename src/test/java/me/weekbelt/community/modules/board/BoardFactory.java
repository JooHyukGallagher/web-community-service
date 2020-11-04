package me.weekbelt.community.modules.board;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class BoardFactory {

    private final BoardRepository boardRepository;

    public Board createRandomBoard(Account account) {
        Board board = Board.builder()
                .boardType(BoardType.FREE)
                .account(account)
                .title("test board")
                .content("board content")
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
        return boardRepository.save(board);
    }

    public void createBoard(Account account, String title, String content, BoardType boardType) {
        Board board = Board.builder()
                .boardType(BoardType.FREE)
                .account(account)
                .title(title)
                .content(content)
                .boardType(boardType)
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
        boardRepository.save(board);
    }
}
