package me.weekbelt.community.modules.board.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.board.form.BoardListElementForm;
import me.weekbelt.community.modules.board.form.BoardSearch;
import me.weekbelt.community.modules.board.form.BoardsResponse;
import me.weekbelt.community.modules.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/v1/boards")
    public ResponseEntity<?> readBoardList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                           BoardSearch boardSearch) {
        Page<BoardListElementForm> boards = boardService.findBoardList(boardSearch, pageable);
        BoardsResponse boardsResponse = BoardsResponse.builder()
                .boards(boards)
                .boardSearch(boardSearch)
                .build();

        return ResponseEntity.ok().body(boardsResponse);
    }
}
