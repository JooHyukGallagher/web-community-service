package me.weekbelt.community.modules.account;

import me.weekbelt.community.modules.account.form.SignUpForm;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class AccountDtoFactory {
    public static Account signUpFormToAccount(SignUpForm signUpForm, PasswordEncoder passwordEncoder) {
        return Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .emailVerified(false)
                .joinedAt(LocalDateTime.now())
                .build();
    }
}
