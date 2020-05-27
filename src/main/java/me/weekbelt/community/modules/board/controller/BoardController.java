package me.weekbelt.community.modules.board.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.board.form.BoardListElementForm;
import me.weekbelt.community.modules.board.form.BoardWriteForm;
import me.weekbelt.community.modules.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards/{boardType}")
    public String boards(@CurrentAccount Account account, @PageableDefault Pageable pageable,
                         @PathVariable String boardType, Model model) {
        model.addAttribute("account", account);
//        Page<BoardListElementForm> boardList = boardService.findBoardList(boardType, pageable);
//        model.addAttribute("boards", boardList.getContent());
        model.addAttribute("boards", boardService.findBoardList(boardType, pageable));
        return "board/" + boardType;
    }

    @GetMapping("/new-board")
    public String createNewBoard(@CurrentAccount Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("board", new BoardWriteForm());
        return "board/creationForm";
    }

//    @PostMapping("/new-board")
//    public String createNewBoardSubmit(@CurrentAccount Account account, @Valid BoardRequestForm boardRequestForm,
//                                       Errors errors, Model model) {
//
//    }
}
