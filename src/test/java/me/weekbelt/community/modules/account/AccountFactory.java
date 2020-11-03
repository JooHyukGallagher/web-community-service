package me.weekbelt.community.modules.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.form.SignUpForm;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.account.service.AccountService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccountFactory {

    private final AccountService accountService;

    public Account createAccount(String nickname) {
        SignUpForm signUpForm = SignUpForm.builder()
                .nickname(nickname)
                .email(nickname + "@email.com")
                .password("12345678")
                .build();
        return accountService.processNewAccount(signUpForm);
    }
}
