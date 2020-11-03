package me.weekbelt.community.modules.account.controller.setting;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@MockMvcTest
public class settingAccountTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("닉네임 수정 폼")
    @WithAccount("joohyuk")
    @Test
    public void updateNickname_form() throws Exception {
        // given
        String requestUri = "/settings/account";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"))
                .andExpect(view().name("account/settings/account"));
    }

    @DisplayName("닉네임 수정 - 입력값 정상")
    @WithAccount("joohyuk")
    @Test
    public void updateNickname_success() throws Exception {
        // given
        String requestUri = "/settings/account";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("nickname", "twins")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/account"))
                .andExpect(flash().attributeExists("message"));

        Account twins = accountRepository.findByNickname("twins").orElse(null);
        assertThat(twins).isNotNull();
        assertThat(twins.getNickname()).isEqualTo("twins");
    }

    @DisplayName("닉네임 수정 - 입력값 실패")
    @WithAccount("joohyuk")
    @ParameterizedTest(name = "{index} {displayName} nickname={0}")
    @ValueSource(strings = {"!#$!@#$@#$", "ab", "joohyuk"})
    @NullAndEmptySource
    public void updateNickname_fail(String nickname) throws Exception {
        // given
        String requestUri = "/settings/account";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("nickname", nickname)
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/account"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"))
                .andExpect(model().hasErrors());
    }

}
