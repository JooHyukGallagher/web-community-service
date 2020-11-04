package me.weekbelt.community.modules.board.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.AccountFactory;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardFactory;
import me.weekbelt.community.modules.board.BoardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class BoardReadTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountFactory accountFactory;

    @Autowired
    BoardFactory boardFactory;

    @BeforeEach
    void initBefore() {
        Account account = accountFactory.createAccount("weekbelt");
        for (int i = 0; i < 17; i++) {
            boardFactory.createBoard(account, "notice " + i,
                    "notice content number " + i, BoardType.NOTICE);
            boardFactory.createBoard(account, "free " + i,
                    "free content number " + i, BoardType.FREE);
            boardFactory.createBoard(account, "question " + i,
                    "question content number " + i, BoardType.QUESTION);
            boardFactory.createBoard(account, "promotion " + i,
                    "promotion content number " + i, BoardType.PROMOTION);
        }
    }

    @Test
    @DisplayName("게시글 리스트 조회")
    public void readBoardList() throws Exception {
        // given
        String requestUri = "/boards";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri)
                .param("searchCondition", "TITLE")
                .param("boardType", "NOTICE")
                .param("keyword", "notice")
                .param("page", "0"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("boards"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attributeExists("boardSearch"))
                .andExpect(view().name("board/boardList"));
    }

    @DisplayName("게시글 조회")
    @WithAccount("joohyuk")
    @Test
    public void readBoard() throws Exception {
        // given
        Account twins = accountFactory.createAccount("twins");
        Board board = boardFactory.createRandomBoard(twins);

        // when
        ResultActions resultActions = mockMvc.perform(get("/boards/" + board.getId() +
                "?boardType=" + board.getBoardType() + "&page=1"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("boardReadForm"))
                .andExpect(model().attributeExists("boardType"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("board/readForm"));
    }

}
