package me.weekbelt.community.modules.board.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.infra.error.ErrorCode;
import me.weekbelt.community.infra.error.GlobalExceptionHandler;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.form.*;
import me.weekbelt.community.modules.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

    @PostMapping
    public ResponseEntity<?> createBoard(@CurrentAccount Account account,
                                         @RequestBody @Valid BoardWriteForm boardWriteForm,
                                         Errors errors) {
        // TODO: Error 메시지 Serialize
        if (errors.hasErrors()) {

            return GlobalExceptionHandler.getErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (boardWriteForm.getBoardType() == BoardType.NOTICE) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                Long boardId = boardService.createBoard(account, boardWriteForm);
                BoardReadForm boardReadForm = boardService.findBoardReadFormById(boardId);
                URI location = linkTo(BoardApiController.class).slash(boardId).toUri();
                return ResponseEntity.created(location).body(boardReadForm);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            Long boardId = boardService.createBoard(account, boardWriteForm);
            BoardReadForm boardReadForm = boardService.findBoardReadFormById(boardId);
            URI location = linkTo(BoardApiController.class).slash(boardId).toUri();
            return ResponseEntity.created(location).body(boardReadForm);
        }
    }

}
