package me.weekbelt.community.modules.board.form;

import lombok.Getter;
import lombok.Setter;
import me.weekbelt.community.modules.board.BoardType;

@Getter @Setter
public class BoardSearch {

    private String keyword;
    private String boardType = "ALL";
    private String searchCondition;
}
