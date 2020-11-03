package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.AccountFactory;
import me.weekbelt.community.modules.account.form.PasswordForm;
import me.weekbelt.community.modules.account.form.Profile;
import me.weekbelt.community.modules.account.form.SignUpForm;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.account.service.AccountService;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@MockMvcTest
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountFactory accountFactory;

    public static class SignUpFormAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return SignUpForm.builder()
                    .nickname(accessor.getString(0))
                    .email(accessor.getString(1))
                    .password(accessor.getString(2))
                    .build();
        }
    }

    public static class ProfileAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return Profile.builder()
                    .bio(accessor.getString(0))
                    .occupation(accessor.getString(1))
                    .location(accessor.getString(2))
                    .profileImage(accessor.getString(3))
                    .build();
        }
    }

    public static class PasswordFormAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return PasswordForm.builder()
                    .newPassword(accessor.getString(0))
                    .newPasswordConfirm(accessor.getString(1))
                    .build();
        }
    }
}