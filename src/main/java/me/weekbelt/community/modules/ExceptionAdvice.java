package me.weekbelt.community.modules;

import lombok.extern.slf4j.Slf4j;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.CurrentAccount;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public String handleRuntimeException(@CurrentAccount Account account,
                                         HttpServletRequest request,
                                         RuntimeException e) {
        if (account != null) {
            log.info("'{}' requested '{}'", account.getNickname(), request.getRequestURI());
        } else {
            log.info("requested '{}'", request.getRequestURI());
        }
        log.error("bad request", e);
        return "error";
    }
}
