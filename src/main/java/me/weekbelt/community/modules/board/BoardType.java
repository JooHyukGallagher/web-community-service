package me.weekbelt.community.modules.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    NOTICE("공지"),
    FREE("자유"),
    QUESTION("질문"),
    PROMOTION("홍보");

    private final String value;

}
