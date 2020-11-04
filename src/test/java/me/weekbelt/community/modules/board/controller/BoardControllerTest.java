package me.weekbelt.community.modules.board.controller;

import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.AccountFactory;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardFactory;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BoardFactory boardFactory;

    @DisplayName("게시글 생성 폼 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void createBoardForm_success() throws Exception {
        // given
        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        assert joohyuk != null;
        joohyuk.completeSignUp();

        // when
        ResultActions resultActions = mockMvc.perform(get("/new-board"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("boardWriteForm"))
                .andExpect(view().name("board/creationForm"));
    }

    @DisplayName("게시글 생성 폼 - 실패(이메일 인증X)")
    @WithAccount("joohyuk")
    @Test
    public void createBoardForm_fail() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/new-board"));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/check-email"));
    }

    @DisplayName("권한 있는 계정의 공지글 생성 - 성공")
    @WithAccount("weekbelt")
    @Test
    public void createNoticeBoard_success() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(post("/new-board")
                .param("boardType", "NOTICE")
                .param("title", "공지 게시글")
                .param("content", "공지 게시글 입니다.")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards?boardType=NOTICE"));
    }

    @DisplayName("권한 없는 계정의 공지글 생성 - 실패")
    @WithAccount("joohyuk")
    @Test
    public void createNoticeBoard_fail() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(post("/new-board")
                .param("boardType", "NOTICE")
                .param("title", "공지 게시글")
                .param("content", "공지 게시글 입니다.")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("boardWriteForm"))
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("board/creationForm"));
    }

    @DisplayName("일반 게시글 생성 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void createBoard_success() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(post("/new-board")
                .param("boardType", "FREE")
                .param("title", "자유 게시글")
                .param("content", "자유 게시글 입니다.")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards?boardType=" + BoardType.FREE));
    }

    @DisplayName("일반 게시글 생성 - 실패(제목 빈칸)")
    @WithAccount("joohyuk")
    @Test
    public void createBoard_fail() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(post("/new-board")
                .param("boardType", "FREE")
                .param("title", "")
                .param("content", "자유 게시글 입니다.")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(view().name("board/creationForm"));
    }



    @DisplayName("게시글 삭제 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void deleteBoard_success() throws Exception {
        // given
        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        Board board = boardFactory.createRandomBoard(joohyuk);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/boards/" + board.getId() + "?boardType=" + board.getBoardType() + "&page=1")
                .param("title", board.getTitle())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/boards?boardType=" + board.getBoardType() + "&page=1"));
    }

    @DisplayName("게시글 삭제 - 실패 (타이틀 똑같이 입력 X)")
    @WithAccount("joohyuk")
    @Test
    public void deleteBoard_fail() throws Exception {
        // given
        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        Board board = boardFactory.createRandomBoard(joohyuk);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/boards/" + board.getId())
                .param("boardType", board.getBoardType().toString())
                .param("page", "1")
                .param("title", "다른 제목")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/boards/" + board.getId() + "?boardType=" + board.getBoardType() + "&page=1"));
    }
}