package me.weekbelt.community.modules.board.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class BoardController {

    @GetMapping("/boards/{boardType}")
    public String boards(@CurrentAccount Account account, Model model,
                         @PathVariable String boardType) {

        model.addAttribute("account", account);
        return "board/" + boardType;
    }
}
