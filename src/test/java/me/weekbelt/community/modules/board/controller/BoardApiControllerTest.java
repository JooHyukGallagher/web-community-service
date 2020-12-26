package me.weekbelt.community.modules.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.AccountFactory;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardFactory;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.form.BoardWriteForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
    AccountRepository accountRepository;

    @Autowired
    AccountFactory accountFactory;

    @Autowired
    BoardFactory boardFactory;

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

    @Test
    @WithAccount("joohyuk")
    @DisplayName("Board 조회")
    void board() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").orElse(null);
        Board board = boardFactory.createRandomBoard(account);
        String requestUri = "/api/v1/boards/" + board.getId() + "?boardType=ALL&page=0";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("boardReadForm").exists())
                .andExpect(jsonPath("boardSearch").exists())
                .andExpect(jsonPath("currentPage").exists())
        ;
    }

    @Test
    @WithAccount("joohyuk")
    @DisplayName("Board 생성 - 성공")
    void createBoard() throws Exception {
        // given
        BoardWriteForm boardWriteForm = BoardWriteForm.builder()
                .title("test title")
                .boardType(BoardType.FREE)
                .content("test content")
                .build();
        String requestUri = "/api/v1/boards";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardWriteForm)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @WithAccount("joohyuk")
    @DisplayName("Board 생성 - 실패")
    void createBoard_fail() throws Exception {
        // given
        BoardWriteForm boardWriteForm = BoardWriteForm.builder()
                .title("")
                .boardType(BoardType.FREE)
                .content("")
                .build();
        String requestUri = "/api/v1/boards";

        // then
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardWriteForm)));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("description").exists());
    }
}