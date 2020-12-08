package me.weekbelt.community.modules.board.form;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class BoardsResponse {
    private final Page<BoardListElementForm> boards;

    private final BoardSearch boardSearch;
}
