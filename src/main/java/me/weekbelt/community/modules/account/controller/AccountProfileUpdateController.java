package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.account.form.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountProfileUpdateController {

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentAccount Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("profile", new Profile(account));
        return "account/settings/profile";
    }
}
