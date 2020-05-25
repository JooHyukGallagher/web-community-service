package me.weekbelt.community.modules.board;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.form.BoardRequestForm;
import me.weekbelt.community.modules.board.form.BoardResponseForm;

import java.time.LocalDateTime;

public class BoardDtoFactory {
    public static Board boardRequestFormToBoard(Account account, BoardRequestForm boardRequestForm){
        return Board.builder()
                .title(boardRequestForm.getTitle())
                .boardType(boardRequestForm.getBoardType())
                .content(boardRequestForm.getContent())
                .account(account)
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
    }

    public static BoardResponseForm boardToBoardResponseForm(Board board) {
        return BoardResponseForm.builder()
                .id(board.getId())
                .title(board.getTitle())
                .boardType(board.getBoardType())
                .content(board.getContent())
                .createdDateTime(board.getCreatedDateTime())
                .viewCount(board.getViewCount())
                .build();
    }
}
