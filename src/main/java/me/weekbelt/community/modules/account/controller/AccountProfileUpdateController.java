package me.weekbelt.community.modules.account.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import me.weekbelt.community.modules.account.form.Profile;
import me.weekbelt.community.modules.account.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class AccountProfileUpdateController {

    private final AccountService accountService;

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentAccount Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("profile", new Profile(account));
        return "account/settings/profile";
    }

    @PostMapping("/settings/profile")
    public String updateProfile(@CurrentAccount Account account, @Valid @ModelAttribute Profile profile,
                                Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "account/settings/profile";
        }

        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        return "redirect:/settings/profile";
    }
}
