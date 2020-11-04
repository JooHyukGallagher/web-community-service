package me.weekbelt.community.modules.board.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardDtoFactory;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.form.*;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import me.weekbelt.community.modules.board.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping("/boards")
    public String boards(@CurrentAccount Account account, Model model,
                         @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                         BoardSearch boardSearch) {

        if (account != null) {
            model.addAttribute("account", account);
        }
        model.addAttribute("boards", boardService.findBoardList(boardSearch, pageable));
        model.addAttribute("boardType", boardSearch.getBoardType());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("boardSearch", boardSearch);

        return "board/boardList";
    }

    @GetMapping("/boards/{id}")
    public String readBoard(@CurrentAccount Account account, @PathVariable Long id,
                            Model model,
                            BoardSearch boardSearch, @RequestParam Integer page) {
        model.addAttribute("account", account);
        model.addAttribute("boardReadForm", boardService.findBoardReadFormById(id));
        model.addAttribute("boardType", boardSearch.getBoardType());
        model.addAttribute("currentPage", page);
        model.addAttribute("boardSearch", boardSearch);
        return "board/readForm";
    }

    @GetMapping("/boards/{id}/update")
    public String updateBoardForm(@CurrentAccount Account account, @PathVariable Long id,
                                  Model model,
                                  @RequestParam String boardType, @RequestParam Integer page) {
        Board board = boardRepository.findBoardWithAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 게시글이 없습니다."));
        BoardUpdateForm boardUpdateForm = BoardDtoFactory.boardToBoardUpdateForm(board);

        model.addAttribute("account", account);
        model.addAttribute("boardUpdateForm", boardUpdateForm);
        model.addAttribute("id", id);
        model.addAttribute("boardType", boardType);
        model.addAttribute("currentPage", page);

        return "board/updateForm";
    }

    @PostMapping("/boards/{id}/update")
    public String updateBoardSubmit(@CurrentAccount Account account, @PathVariable Long id,
                                    @Valid BoardUpdateForm boardUpdateForm, Errors errors,
                                    Model model, @RequestParam Integer page) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("boardType", boardUpdateForm.getBoardType());
            model.addAttribute("currentPage", page);
            return "board/updateForm";
        }

        if (boardUpdateForm.getBoardType() == BoardType.NOTICE) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                boardService.updateBoard(id, boardUpdateForm);
                return "redirect:/boards/" + id + "?boardType=" + boardUpdateForm.getBoardType() + "&page=" + page;
            } else {
                model.addAttribute("account", account);
                model.addAttribute("boardUpdateForm", boardUpdateForm);
                model.addAttribute("message", "게시글을 공지로 수정 권한이 없습니다.");
                model.addAttribute("boardType", boardUpdateForm.getBoardType());
                model.addAttribute("currentPage", page);
                return "board/updateForm";
            }
        } else {
            boardService.updateBoard(id, boardUpdateForm);
            return "redirect:/boards/" + id + "?boardType=" + boardUpdateForm.getBoardType() + "&page=" + page;
        }
    }

    @GetMapping("/new-board")
    public String createNewBoard(@CurrentAccount Account account, Model model) {
        if (!account.isEmailVerified()) {
            return "redirect:/check-email";
        }
        model.addAttribute("account", account);
        model.addAttribute("boardWriteForm", new BoardWriteForm());
        return "board/creationForm";
    }

    @PostMapping("/new-board")
    public String createNewBoardSubmit(@CurrentAccount Account account,
                                       @Valid BoardWriteForm boardWriteForm,
                                       Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "board/creationForm";
        }

        if (boardWriteForm.getBoardType() == BoardType.NOTICE) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                boardService.createBoard(account, boardWriteForm);
                return "redirect:/boards?boardType=" + boardWriteForm.getBoardType();
            } else {
                model.addAttribute("account", account);
                model.addAttribute("boardWriteForm", boardWriteForm);
                model.addAttribute("message", "공지글을 작성할 권한이 없습니다.");
                return "board/creationForm";
            }
        } else {
            boardService.createBoard(account, boardWriteForm);
            return "redirect:/boards?boardType=" + boardWriteForm.getBoardType();
        }
    }

    @DeleteMapping("/boards/{id}")
    public String deleteBoard(@PathVariable Long id, @RequestParam String title,
                              @RequestParam String boardType, @RequestParam Integer page,
                              RedirectAttributes attributes) {
        Board board = boardRepository.findById(id).orElse(null);
        assert board != null;
        if (title.equals(board.getTitle())) {
            boardService.removeBoard(board);
            attributes.addFlashAttribute("message", "게시글을 삭제했습니다.");
            return "redirect:/boards?boardType=" + boardType + "&page=" + page;
        } else {
            attributes.addFlashAttribute("message", "게시글 제목을 정확히 입력해주세요.");
            return "redirect:/boards/" + board.getId() + "?boardType=" + boardType + "&page=" + page;
        }
    }
}
