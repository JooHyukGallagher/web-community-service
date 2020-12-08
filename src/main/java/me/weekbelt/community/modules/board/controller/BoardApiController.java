package me.weekbelt.community.modules.board.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.board.form.*;
import me.weekbelt.community.modules.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<?> readBoardList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           BoardSearch boardSearch) {
        Page<BoardListElementForm> boards = boardService.findBoardList(boardSearch, pageable);
        BoardsResponse boardsResponse = BoardsResponse.builder()
                .boards(boards)
                .boardSearch(boardSearch)
                .build();

        return ResponseEntity.ok().body(boardsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readBoard(@PathVariable Long id, BoardSearch boardSearch, @RequestParam Integer page) {
        BoardReadForm boardReadForm = boardService.findBoardReadFormById(id);
        BoardResponse boardResponse = BoardResponse.builder()
                .boardReadForm(boardReadForm)
                .boardSearch(boardSearch)
                .currentPage(page)
                .build();

        return ResponseEntity.ok().body(boardResponse);
    }
}
