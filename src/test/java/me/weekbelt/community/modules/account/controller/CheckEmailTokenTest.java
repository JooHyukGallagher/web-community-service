package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.form.SignUpForm;
import me.weekbelt.community.modules.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class CheckEmailTokenTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @DisplayName("인증 메일 확인 - 입력값 오류")
    @ParameterizedTest(name = "{index} {displayName} token={0} email={1}")
    @CsvSource({
            "'asdfasdf', 'asdfas@sdf.com'",
            "'awefsdf', 'twins@gmail.com'",
            "'', 'twins@gmail.com'",
            "'asdfasdfa', ''",
            "null, null"
    })
    public void checkEmailToken_with_wrong_input(String token, String email) throws Exception {
        // given
        String requestUri = "/check-email-token";

        // 회원가입 (이메일 인증 토큰 생성)
        SignUpForm signUpForm = SignUpForm.builder()
                .nickname("twins")
                .email("twins@gmail.com")
                .password("12345678")
                .build();
        accountService.processNewAccount(signUpForm);

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri)
                .param("token", token)
                .param("email", email));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(unauthenticated());
    }

    @DisplayName("인증 메일 확인 - 입력값 정상")
    @Test
    public void checkEmailToken_with_correct_input() throws Exception {
        // given
        SignUpForm signUpForm = SignUpForm.builder()
                .email("test@email.com")
                .password("12345678")
                .nickname("joohyuk")
                .build();
        Account newAccount = accountService.processNewAccount(signUpForm);

        String requestUri = "/check-email-token";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri)
                .param("token", newAccount.getEmailCheckToken())
                .param("email", newAccount.getEmail()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated().withUsername(newAccount.getNickname()));
    }

}
