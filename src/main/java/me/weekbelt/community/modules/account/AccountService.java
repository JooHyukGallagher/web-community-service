package me.weekbelt.community.modules.account;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.infra.mail.EmailMessage;
import me.weekbelt.community.infra.mail.EmailService;
import me.weekbelt.community.modules.account.form.SignUpForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .emailVerified(false)
                .joinedAt(LocalDateTime.now())
                .build();
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    private void sendSignUpConfirmEmail(Account newAccount) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("회원 가입 인증")
                .message("회원가입 인증을 해주세요.")
                .build();
        emailService.sendEmail(emailMessage);
    }
}
