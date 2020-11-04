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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class BoardUpdateTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BoardFactory boardFactory;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    AccountFactory accountFactory;

    @DisplayName("게시글 수정 폼 조회 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void updateBoardForm_success() throws Exception {
        // given
        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        Board board = boardFactory.createRandomBoard(joohyuk);
        String requestUri = "/boards/" + board.getId() + "/update?boardType=" + board.getBoardType() + "&page=1";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUri));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("boardUpdateForm"))
                .andExpect(model().attributeExists("id"))
                .andExpect(model().attributeExists("boardType"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("board/updateForm"));
    }

    @DisplayName("게시글 수정 폼 조회 - 실패(권한 X)")
    @WithAccount("joohyuk")
    @Test
    public void updateBoardForm_fail() throws Exception {
        // given
        Account account = accountFactory.createAccount("twins");
        Board board = boardFactory.createRandomBoard(account);
        String requestUri = "/boards/" + board.getId() + "/update?boardType=" + board.getBoardType() + "&page=1";

        // when & then
        Assertions.assertThrows(Exception.class, () -> {
            mockMvc.perform(get(requestUri));
        });
    }

    @DisplayName("ADMIN 계정의 게시글을 공지글로 수정 - 성공")
    @WithAccount("weekbelt")
    @Test
    public void updateNoticeBoard_success() throws Exception {
        // given
        Account weekbelt = accountRepository.findByNickname("weekbelt").orElse(null);
        Board board = boardFactory.createRandomBoard(weekbelt);

        // when
        ResultActions resultActions = mockMvc.perform(post("/boards/" + board.getId() + "/update")
                .param("title", "수정 공지 게시글")
                .param("content", "공지로 수정한 게시글 입니다.")
                .param("boardType", "NOTICE")
                .param("page", "1")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + board.getId() + "?boardType=" + board.getBoardType() + "&page=1"));

        Board updatedBoard = boardRepository.findById(board.getId()).orElse(null);
        assert updatedBoard != null;
        assertThat(updatedBoard.getTitle()).isEqualTo("수정 공지 게시글");
        assertThat(updatedBoard.getContent()).isEqualTo("공지로 수정한 게시글 입니다.");
        assertThat(updatedBoard.getBoardType()).isEqualTo(BoardType.NOTICE);
    }

    @DisplayName("ADMIN 계정의 게시글을 공지글로 수정 - 실패")
    @WithAccount("joohyuk")
    @Test
    public void updateNoticeBoard_fail() throws Exception {
        // given
        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        Board board = boardFactory.createRandomBoard(joohyuk);

        // when
        ResultActions resultActions = mockMvc.perform(post("/boards/" + board.getId() + "/update?page=1")
                .param("title", "수정 공지 게시글")
                .param("content", "공지로 수정한 게시글 입니다.")
                .param("boardType", "NOTICE")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("boardUpdateForm"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("boardType"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("board/updateForm"));

        Board noneUpdatedBoard = boardRepository.findById(board.getId()).orElse(null);
        assert noneUpdatedBoard != null;
        assertThat(noneUpdatedBoard.getTitle()).isEqualTo("test board");
        assertThat(noneUpdatedBoard.getContent()).isEqualTo("board content");
        assertThat(noneUpdatedBoard.getBoardType()).isEqualTo(BoardType.FREE);
    }


    @DisplayName("게시글 수정 - 성공")
    @WithAccount("joohyuk")
    @Test
    public void updateBoard_success() throws Exception {
        // given
        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        Board board = boardFactory.createRandomBoard(joohyuk);

        // when
        ResultActions resultActions = mockMvc.perform(post("/boards/" + board.getId() + "/update")
                .param("title", "수정한 제목")
                .param("content", "수정한 내용 입니다.")
                .param("boardType", "PROMOTION")
                .param("page", "1")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/" + board.getId() + "?boardType=" + board.getBoardType() + "&page=1"));

        Board updatedBoard = boardRepository.findById(board.getId()).orElse(null);
        assert updatedBoard != null;
        assertThat(updatedBoard.getTitle()).isEqualTo("수정한 제목");
        assertThat(updatedBoard.getContent()).isEqualTo("수정한 내용 입니다.");
        assertThat(updatedBoard.getBoardType()).isEqualTo(BoardType.PROMOTION);
    }

    @DisplayName("게시글 수정 - 실패(제목 공백)")
    @WithAccount("joohyuk")
    @Test
    public void updateBoard_fail() throws Exception {
        // given
        Account joohyuk = accountRepository.findByNickname("joohyuk").orElse(null);
        Board board = boardFactory.createRandomBoard(joohyuk);

        // when
        ResultActions resultActions = mockMvc.perform(post("/boards/" + board.getId() + "/update")
                .param("title", "")
                .param("content", "수정한 내용 입니다.")
                .param("boardType", "PROMOTION")
                .param("page", "1")
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("boardType"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("board/updateForm"));
    }
}
