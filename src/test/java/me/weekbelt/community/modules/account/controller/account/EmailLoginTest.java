package me.weekbelt.community.modules.account.controller.account;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.AccountFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class EmailLoginTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountFactory accountFactory;


    @Test
    @DisplayName("이메일 로그인 화면")
    void emailLoginForm() throws Exception {
        // given
        String requestUri = "/email-login";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("account/email-login"));
    }

    @DisplayName("이메일 로그인 링크 요청 - 실패(존재하지 않는 이메일주소)")
    @ParameterizedTest(name = "{index} {displayName} email={0}")
    @ValueSource(strings = {"asdf@asdf.com", "weekbelt@email.com"})
    void emailLogin_fail(String email) throws Exception {
        // given
        accountFactory.createAccount("weekbelt");
        String requestUri = "/email-login";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("email", email)
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/email-login"));
    }

    // TODO: 이메일 로그인 링크 요청 성공

    @ParameterizedTest(name = "{index} {displayName} token={0} email={1}")
    @CsvSource({
            "'asdfasdf', 'weekbelt@email.com'",     // 토큰이 다른 경우
            "'asdfasdf', 'asdf@email.com'"          // 이메일이 존재하지 않는 경우
    })
    @DisplayName("이메일 로그인 - 실패")
    void loginByEmail_fail(String token, String email) throws Exception {
        // given
         accountFactory.createAccount("weekbelt");
        String requestUri = "/login-by-email";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri)
                .param("token", token)
                .param("email", email));

        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/logged-in-by-email"));

    }

    @Test
    @DisplayName("이메일 로그인 - 성공")
    void loginByEmail_success() throws Exception {
        // given
        Account account = accountFactory.createAccount("weekbelt");
        String requestUri = "/login-by-email";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri)
                .param("token", account.getEmailCheckToken())
                .param("email", account.getEmail()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("account/logged-in-by-email"));
    }
}
