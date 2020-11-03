package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.controller.AccountControllerTest.SignUpFormAggregator;
import me.weekbelt.community.modules.account.form.SignUpForm;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class SignUpTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("회원 가입 화면 폼")
    @Test
    public void signUpForm() throws Exception {
        // given
        String requestUri = "/sign-up";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @ParameterizedTest(name = "{index} {displayName} nickname={0} email={1} password={2}")
    @CsvSource({
            "'', 'twins1@gmail.com', '12345678'",
            "'as', 'twins2@gmail.com', '12345678'",
            "'twins', '', '12345678'",
            "'twins', 'twins1@gmail.com', '12345'",
    })
    public void signUpSubmit_with_wrong_input(@AggregateWith(SignUpFormAggregator.class) SignUpForm signUpForm) throws Exception {
        // given
        String requestUri = "/sign-up";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("nickname", signUpForm.getNickname())
                .param("email", signUpForm.getEmail())
                .param("password", signUpForm.getPassword())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 - 입력값 정상")
    @ParameterizedTest
    @CsvSource({"'joohyuk', 'vfrvfr4207@hanmail.net', '12345678'"})
    public void signUpSubmit_with_correct_input(@AggregateWith(SignUpFormAggregator.class) SignUpForm signUpForm) throws Exception {
        // given
        String requestUri = "/sign-up";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("nickname", signUpForm.getNickname())
                .param("email", signUpForm.getEmail())
                .param("password", signUpForm.getPassword())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername(signUpForm.getNickname()));

        Account account = accountRepository.findByEmail(signUpForm.getEmail()).orElse(null);
        assertThat(account).isNotNull();
        assertThat(account.getEmailCheckToken()).isNotNull();
        assertThat(accountRepository.existsByEmail(signUpForm.getEmail())).isTrue();
    }

}
