package me.weekbelt.community.modules.board.form;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardResponse {
    private final BoardReadForm boardReadForm;

    private final BoardSearch boardSearch;

    private final Integer currentPage;
}
