package me.weekbelt.community.modules.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class BoardApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Boards 조회")
    void boards() throws Exception {
        // given
        String requestUri = "/api/v1/boards?boardType=ALL";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("boards").exists())
                .andExpect(jsonPath("boardSearch").exists());
    }

}