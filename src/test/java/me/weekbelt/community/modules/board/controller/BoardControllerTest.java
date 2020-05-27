package me.weekbelt.community.modules.board.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("게시글 생성 폼")
    @WithAccount("joohyuk")
    @Test
    public void createBoardForm() throws Exception {
        mockMvc.perform(get("/new-board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("board"))
                .andExpect(view().name("board/creationForm"));
    }
}