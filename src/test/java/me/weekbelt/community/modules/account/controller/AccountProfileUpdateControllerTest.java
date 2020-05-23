package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountProfileUpdateControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;

    @DisplayName("프로필 수정 폼")
    @WithAccount("joohyuk")
    @Test
    public void updateProfileForm() throws Exception {
        mockMvc.perform(get("/settings/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(view().name("account/settings/profile"));
    }

    @DisplayName("프로필 수정 하기 - 입력값 정상")
    @WithAccount("joohyuk")
    @Test
    public void updateProfile() throws Exception {
        String bio = "짧은 소개 입니다.";
        String occupation = "프로그래머";
        String location = "수지구";

        mockMvc.perform(post("/settings/profile")
                .param("bio", bio)
                .param("occupation", occupation)
                .param("location", location)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));

        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        assertThat(joohyuk.getBio()).isEqualTo(bio);
        assertThat(joohyuk.getOccupation()).isEqualTo(occupation);
        assertThat(joohyuk.getLocation()).isEqualTo(location);
    }

    @DisplayName("프로필 수정 하기 - 입력값 에러")
    @WithAccount("joohyuk")
    @Test
    public void updateProfile_error() throws Exception {
        String bio = "짧은 소개 인데 입력값의 길이 제한을 넘겨버리는 경우, 길게 소개를 수정하는 경우";

        mockMvc.perform(post("/settings/profile")
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/settings/profile"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors())
        ;

        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        assertThat(joohyuk.getBio()).isNull();
    }
}