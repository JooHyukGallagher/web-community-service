package me.weekbelt.community.modules.board.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.form.BoardReadForm;
import me.weekbelt.community.modules.board.form.BoardWriteForm;
import me.weekbelt.community.modules.board.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public String boards(@CurrentAccount Account account, @PageableDefault Pageable pageable,
                         @RequestParam(defaultValue = "ALL") String boardType, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("boards", boardService.findBoardList(boardType, pageable));
        model.addAttribute("boardType", boardType);
        return "board/" + boardType;
    }

    @GetMapping("/boards/{id}")
    public String readBoard(@CurrentAccount Account account, @PathVariable Long id,
                            Model model) {
        model.addAttribute("account", account);
        model.addAttribute("boardReadForm", boardService.findBoardReadFormById(id));
        return "board/readForm";
    }

    @GetMapping("/boards/{id}/update")
    public String updateBoardForm(@CurrentAccount Account account, @PathVariable Long id,
                                  Model model) {
        BoardReadForm boardReadForm = boardService.findBoardReadFormById(id);
        BoardWriteForm boardWriteForm = BoardWriteForm.builder()
                .title(boardReadForm.getTitle())
                .boardType(boardReadForm.getBoardType())
                .content(boardReadForm.getContent())
                .build();
        model.addAttribute("account", account);
        model.addAttribute("boardWriteForm", boardWriteForm);
        model.addAttribute("id", id);
        return "board/updateForm";
    }

//    @PostMapping("/boards/{id}/update")
//    public String updateBoardSubmit(@CurrentAccount Account account, @PathVariable Long id,
//                                    @Valid BoardWriteForm boardWriteForm,
//                                    Model model){
//
//    }

    @GetMapping("/new-board")
    public String createNewBoard(@CurrentAccount Account account, Model model) {
        if (!account.isEmailVerified()) {
            return "redirect:/check-email";
        }
        model.addAttribute("account", account);
        model.addAttribute("board", new BoardWriteForm());
        return "board/creationForm";
    }

    @PostMapping("/new-board")
    public String createNewBoardSubmit(@CurrentAccount Account account,
                                       @Valid BoardWriteForm boardWriteForm,
                                       Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("board", boardWriteForm);
            return "board/creationForm";
        }

        if (boardWriteForm.getBoardType() == BoardType.NOTICE) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                boardService.createBoard(account, boardWriteForm);
                return "redirect:/boards";
            } else {
                model.addAttribute("account", account);
                model.addAttribute("board", boardWriteForm);
                model.addAttribute("message", "공지글을 작성할 권한이 없습니다.");
                return "board/creationForm";
            }
        } else {
            boardService.createBoard(account, boardWriteForm);
            return "redirect:/boards";
        }
    }
}
