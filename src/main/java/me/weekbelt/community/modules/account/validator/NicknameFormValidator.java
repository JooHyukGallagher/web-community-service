package me.weekbelt.community.modules.account.validator;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.form.NicknameForm;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class NicknameFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) target;
        accountRepository.findByNickname(nicknameForm.getNickname())
                .ifPresent(account -> errors.rejectValue("nickname", "wrong.value",
                "입력하신 닉네임을 사용할 수 없습니다."));
    }
}
