package me.weekbelt.community.modules.account.controller.setting;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.controller.AccountControllerTest.PasswordFormAggregator;
import me.weekbelt.community.modules.account.form.PasswordForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class settingPasswordTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("패스워드 수정 폼")
    @WithAccount("joohyuk")
    @Test
    public void updatePassword_form() throws Exception {
        // given
        String requestUri = "/settings/password";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(view().name("account/settings/password"));
    }

    @DisplayName("패스워드 수정 - 성공")
    @WithAccount("joohyuk")
    @ParameterizedTest(name = "{index} {displayName} newPassword={0} newPasswordConfirm={1}")
    @CsvSource({
            "'12345678', '12345678'",
            "'비밀번호 입력하기', '비밀번호 입력하기'",
    })
    public void updatePassword_success(@AggregateWith(PasswordFormAggregator.class) PasswordForm passwordForm) throws Exception {
        // given
        String requestUri = "/settings/password";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("newPassword", passwordForm.getNewPassword())
                .param("newPasswordConfirm", passwordForm.getNewPasswordConfirm())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/password"))
                .andExpect(flash().attributeExists("message"));
    }

    @DisplayName("패스워드 수정 - 실패")
    @WithAccount("joohyuk")
    @ParameterizedTest(name = "{index} {displayName} newPassword={0} newPasswordConfirm={1}")
    @CsvSource({
            "'1234567890', '0987654321'",
            "'1234', '1234'",
            "null, null",
            "'', ''"
    })
    public void updatePassword_fail(@AggregateWith(PasswordFormAggregator.class) PasswordForm passwordForm) throws Exception {
        // given
        String requestUri = "/settings/password";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("newPassword", passwordForm.getNewPassword())
                .param("newPasswordConfirm", passwordForm.getNewPasswordConfirm())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("account/settings/password"))
                .andExpect(model().attributeExists("account"));
    }
}
