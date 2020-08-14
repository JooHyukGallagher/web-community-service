package me.weekbelt.community.modules.reply;

import me.weekbelt.community.modules.account.Account;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ReplyWriteValidator {
    public void validate(Account account, Errors errors) {
        if (!account.isEmailVerified()) {
            errors.reject("notEmailVerified", "Community 게시판 서비스를 사용하려면 인증 이메일을 확인하세요.");
        }
    }
}
