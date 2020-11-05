package me.weekbelt.community.modules.board;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.form.BoardListElementForm;
import me.weekbelt.community.modules.board.form.BoardUpdateForm;
import me.weekbelt.community.modules.board.form.BoardWriteForm;
import me.weekbelt.community.modules.board.form.BoardReadForm;

import java.time.LocalDateTime;

public class BoardDtoFactory {
    public static Board boardWriteFormToBoard(Account account, BoardWriteForm boardWriteForm){
        return Board.builder()
                .title(boardWriteForm.getTitle())
                .boardType(boardWriteForm.getBoardType())
                .content(boardWriteForm.getContent())
                .account(account)
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
    }

    public static BoardListElementForm boardToBoardListElementForm(Board board) {
        return BoardListElementForm.builder()
                .id(board.getId())
                .title(board.getTitle())
                .nickname(board.getAccount().getNickname())
                .commentCount(board.getReplyList().size())
                .createdDateTime(board.getCreatedDateTime())
                .viewCount(board.getViewCount())
                .build();
    }

    public static BoardReadForm boardToBoardReadForm(Board board) {
       return BoardReadForm.builder()
               .id(board.getId())
               .boardType(board.getBoardType())
               .title(board.getTitle())
               .content(board.getContent())
               .createdDateTime(board.getCreatedDateTime())
               .viewCount(board.getViewCount())
               .nickname(board.getAccount().getNickname())
               .profileImage(board.getAccount().getProfileImage())
               .build();
    }

    public static BoardUpdateForm boardToBoardUpdateForm (Board board) {
        return BoardUpdateForm.builder()
                .title(board.getTitle())
                .boardType(board.getBoardType())
                .content(board.getContent())
                .build();
    }
}
