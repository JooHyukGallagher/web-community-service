package me.weekbelt.community.modules.account.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.AccountFactory;
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
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountFactory accountFactory;

    static class SignUpFormAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return SignUpForm.builder()
                    .nickname(accessor.getString(0))
                    .email(accessor.getString(1))
                    .password(accessor.getString(2))
                    .build();
        }
    }
}