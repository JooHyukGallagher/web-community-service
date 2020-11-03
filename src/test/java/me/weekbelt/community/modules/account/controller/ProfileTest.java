package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.AccountFactory;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class ProfileTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountFactory accountFactory;

    @Test
    @WithAccount("joohyuk")
    @DisplayName("사용자 정보 - 내 정보 ")
    void myProfile() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").orElse(null);
        String requestUri = "/profile/" + account.getNickname();

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attribute("account", account))
                .andExpect(model().attribute("isOwner", true))
                .andExpect(view().name("account/profile"));

    }

    @Test
    @WithAccount("joohyuk")
    @DisplayName("사용자 정보 - 다른 사람 정보")
    void otherProfile() throws Exception {
        // given
        Account account = accountFactory.createAccount("weekbelt");
        String requestUri = "/profile/" + account.getNickname();

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attribute("account", account))
                .andExpect(model().attribute("isOwner", false))
                .andExpect(view().name("account/profile"));

    }

}
