package me.weekbelt.community.infra.mail;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.account.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Component
public class EmailUtilService {

    @Value("${app.host}")
    private String host;

    private final TemplateEngine templateEngine;

    public String createSignUpConfirmEmailHtmlMessage(Account account) {
        Context context = new Context();
        context.setVariable("link",  "/check-email-token?token=" +
                account.getEmailCheckToken() + "&email=" + account.getEmail());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "Community 게시판 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", host);

        // mail/simple-link.html을 String 타입으로 생성
        return templateEngine.process("mail/simple-link", context);
    }

    public String createLoginLinkHtmlMessage(Account account) {
        Context context = new Context();
        context.setVariable("link",  "/login-by-email?token=" +
                account.getEmailCheckToken() + "&email=" + account.getEmail());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "Community 게시판 서비스 로그인 하기");
        context.setVariable("message", "로그인하려면 아래 링크를 클릭하세요.");
        context.setVariable("host", host);

        // mail/simple-link.html을 String 타입으로 생성
        return templateEngine.process("mail/simple-link", context);
    }
}
