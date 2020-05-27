package me.weekbelt.community.modules.board;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.form.BoardWriteForm;
import me.weekbelt.community.modules.board.form.BoardReadForm;

import java.time.LocalDateTime;

public class BoardDtoFactory {
    public static Board boardRequestFormToBoard(Account account, BoardWriteForm boardWriteForm){
        return Board.builder()
                .title(boardWriteForm.getTitle())
                .boardType(boardWriteForm.getBoardType())
                .content(boardWriteForm.getContent())
                .account(account)
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
    }

    public static BoardReadForm boardToBoardResponseForm(Board board) {
        return BoardReadForm.builder()
                .id(board.getId())
                .title(board.getTitle())
                .boardType(board.getBoardType())
                .content(board.getContent())
                .createdDateTime(board.getCreatedDateTime())
                .viewCount(board.getViewCount())
                .build();
    }
}
