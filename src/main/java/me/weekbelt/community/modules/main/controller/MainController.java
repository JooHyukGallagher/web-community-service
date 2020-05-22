package me.weekbelt.community.modules.main.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping("/")
    public String index(@CurrentAccount Account account, Model model){
        if (account != null) {
            model.addAttribute("account", account);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
