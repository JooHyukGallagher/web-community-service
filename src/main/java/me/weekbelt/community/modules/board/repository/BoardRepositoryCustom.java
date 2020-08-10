package me.weekbelt.community.modules.board.repository;

import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.form.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findByBoardSearch(BoardSearch boardSearch, Pageable pageable);
}
