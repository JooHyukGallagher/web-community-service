package me.weekbelt.community.modules.account.controller.setting;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.controller.AccountControllerTest.ProfileAggregator;
import me.weekbelt.community.modules.account.form.Profile;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@MockMvcTest
public class settingProfileTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @DisplayName("프로필 수정 폼")
    @WithAccount("joohyuk")
    @Test
    public void updateProfileForm() throws Exception {
        // given
        String requestUri = "/settings/profile";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(view().name("account/settings/profile"));
    }

    @DisplayName("프로필 수정 하기 - 입력값 정상")
    @WithAccount("joohyuk")
    @ParameterizedTest(name = "{displayName} bio={0} occupation={1} location={2}")
    @CsvSource({
            "null, 'SoftwareEngineer', '용인시 수지구', null",
            "'안녕하세요', null, null, null",
            "null, null, '용인시 수지구', null",
            "'만나서 반갑습니다.', null, '용인시 수지구', null",
    })
    public void updateProfile_success(@AggregateWith(ProfileAggregator.class) Profile profile) throws Exception {
        // given
        String requestUri = "/settings/profile";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("bio", profile.getBio())
                .param("occupation", profile.getOccupation())
                .param("location", profile.getLocation())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));

        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        assert joohyuk != null;
        assertThat(joohyuk.getBio()).isEqualTo(profile.getBio());
        assertThat(joohyuk.getOccupation()).isEqualTo(profile.getOccupation());
        assertThat(joohyuk.getLocation()).isEqualTo(profile.getLocation());
    }

    @DisplayName("프로필 수정 하기 - 입력값 에러")
    @WithAccount("joohyuk")
    @ParameterizedTest(name = "{displayName} bio={0} occupation={1} location={2}")
    @CsvSource({
            "'짧은 소개 인데 입력값의 길이 제한을 넘겨버리는 경우, 길게 소개를 수정하는 경우', null, null, null",
    })
    public void updateProfile_fail(@AggregateWith(ProfileAggregator.class) Profile profile) throws Exception {
        // given
        String requestUri = "/settings/profile";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("bio", profile.getBio())
                .param("occupation", profile.getOccupation())
                .param("location", profile.getLocation())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().hasErrors())
                .andExpect(view().name("account/settings/profile"));

    }
//    @DisplayName("프로필 수정 하기 - 입력값 에러")
//    @WithAccount("joohyuk")
//    @Test
//    public void updateProfile_error() throws Exception {
//        String bio = "짧은 소개 인데 입력값의 길이 제한을 넘겨버리는 경우, 길게 소개를 수정하는 경우";
//
//        mockMvc.perform(post("/settings/profile")
//                .param("bio", bio)
//                .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("account/settings/profile"))
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("profile"))
//                .andExpect(model().hasErrors())
//        ;
//
//        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
//        assertThat(joohyuk.getBio()).isNull();
//    }
}
