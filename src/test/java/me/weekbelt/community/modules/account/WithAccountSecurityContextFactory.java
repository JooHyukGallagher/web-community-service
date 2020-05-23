package me.weekbelt.community.modules.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.form.SignUpForm;
import me.weekbelt.community.modules.account.service.AccountService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String nickname = withAccount.value();

        // 회원 가입
        SignUpForm signUpForm = SignUpForm.builder()
                .nickname(nickname)
                .email(nickname + "@gmail.com")
                .password("12345678")
                .build();
        accountService.processNewAccount(signUpForm);

        // Authentication 객체를 SecurityContext에 넣어주기
        UserDetails principal = accountService.loadUserByUsername(nickname);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        return context;
    }
}
