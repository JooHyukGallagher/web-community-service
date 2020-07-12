package me.weekbelt.community.modules.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.weekbelt.community.infra.mail.EmailMessage;
import me.weekbelt.community.infra.mail.EmailService;
import me.weekbelt.community.infra.mail.EmailUtilService;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.Role;
import me.weekbelt.community.modules.account.UserAccount;
import me.weekbelt.community.modules.account.form.Profile;
import me.weekbelt.community.modules.account.form.SignUpForm;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailUtilService emailUtilService;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.createAccountFromSignUpForm(signUpForm, passwordEncoder);
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        String message = emailUtilService.createSignUpConfirmEmailHtmlMessage(newAccount);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("Community 게시판 서비스, 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }


    public void login(Account account) {
        Set<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(account);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account, grantedAuthorities),
                account.getPassword(),
                grantedAuthorities);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public void sendLoginLink(Account account) {
        String message = emailUtilService.createLoginLinkHtmlMessage(account);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("Community 게시판 서비스, 로그인 링크")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname).orElse(null);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname)
                    .orElseThrow(() -> new UsernameNotFoundException(emailOrNickname + "에 해당하는 유저가 없습니다."));
        }

        Set<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(account);
        return new UserAccount(account, grantedAuthorities);
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Account account) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (account.getId() == 1 || account.getNickname().equals("weekbelt")) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }
        return grantedAuthorities;
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    public void updateProfile(Account account, Profile profile) {
        account.updateProfile(profile);
        accountRepository.save(account);
    }

    public void updatePassword(Account account, String newPassword) {
        account.updatePassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String nickname) {
        account.updateNickname(nickname);
        accountRepository.save(account);
        login(account);
    }

}
