package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class ResendConfirmEmailTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAccount("joohyuk")
    @DisplayName("이메일 인증 재전송 - 실패(1분 지나기 전에 재전송 요청)")
    void resendConfirmEmail_fail() throws Exception {
        // given
        String requestUri = "/resend-confirm-email";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("email"))
                .andExpect(view().name("account/check-email"));


    }

    // TODO: 이메일 인증 재전송 성공

}
