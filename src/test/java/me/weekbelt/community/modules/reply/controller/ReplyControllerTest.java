package me.weekbelt.community.modules.reply.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.community.infra.MockMvcTest;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.WithAccount;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardFactory;
import me.weekbelt.community.modules.reply.Reply;
import me.weekbelt.community.modules.reply.ReplyFactory;
import me.weekbelt.community.modules.reply.form.ReplyCreateForm;
import me.weekbelt.community.modules.reply.form.ReplyReadForm;
import me.weekbelt.community.modules.reply.form.ReplyUpdateForm;
import me.weekbelt.community.modules.reply.repository.ReplyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class ReplyControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BoardFactory boardFactory;
    @Autowired
    ReplyFactory replyFactory;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ReplyRepository replyRepository;

    @DisplayName("댓글 추가 API - 성공")
    @WithAccount("joohyuk")
    @Test
    void createReply_success() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").get();
        account.completeSignUp(); // 이메일 검증
        Board board = boardFactory.createBoard(account);
        ReplyCreateForm replyCreateform = ReplyCreateForm.builder()
                .content("test reply")
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();

        String requestUrl = "/boards/" + board.getId() + "/replies";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(replyCreateform))
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();

        ReplyReadForm replyReadForm = objectMapper.readValue(contentAsString, ReplyReadForm.class);

        assertThat(replyReadForm.getContent()).isEqualTo("test reply");
        assertThat(replyReadForm.getBoardId()).isEqualTo(board.getId());
        assertThat(replyReadForm.getNickname()).isEqualTo(account.getNickname());
    }

    @DisplayName("댓글 추가 API - 실패(내용 입력 x)")
    @WithAccount("joohyuk")
    @Test
    void createReply_fail_forNotContent() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").get();
        account.completeSignUp();       // 이메일 검증
        Board board = boardFactory.createBoard(account);
        ReplyCreateForm replyCreateform = ReplyCreateForm.builder()
                .content("")
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();

        String requestUrl = "/boards/" + board.getId() + "/replies";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(replyCreateform))
                .with(csrf()));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

//    TODO: 이메일 확인 로직 처리 후
//    @DisplayName("댓글 추가 API - 실패(이메일 확인 x)")
//    @WithAccount("joohyuk")
//    @Test
//    void createReply_fail_forNotEmailVerified() throws Exception {
//        // given
//        Account account = accountRepository.findByNickname("joohyuk").get();
//        Board board = boardFactory.createBoard(account);
//        ReplyCreateForm replyCreateform = ReplyCreateForm.builder()
//                .content("test reply")
//                .createdDateTime(LocalDateTime.now())
//                .modifiedDateTime(LocalDateTime.now())
//                .build();
//
//        String requestUrl = "/boards/" + board.getId() + "/replies";
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post(requestUrl)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(replyCreateform))
//                .with(csrf()));
//
//        // then
//        resultActions
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].code").exists())
//                .andExpect(jsonPath("$[0].defaultMessage").exists());
//    }

    @Test
    @WithAccount("joohyuk")
    @DisplayName("댓글 리스트 불러오기")
    void replyList() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").get();
        Board board = boardFactory.createBoard(account);

        for (int i = 1; i <= 36; i++) {
            ReplyCreateForm replyCreateForm = ReplyCreateForm.builder()
                    .content("reply test " + i)
                    .createdDateTime(LocalDateTime.now())
                    .modifiedDateTime(LocalDateTime.now())
                    .build();
            replyFactory.createReply(account, board, replyCreateForm);
        }

        // when
        String requestUrl = "/boards/" + board.getId() + "/replies?page=1&sort=id,desc";
        ResultActions resultActions = mockMvc.perform(get(requestUrl));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk());

        // TODO: Page 직렬
//        MockHttpServletResponse response = resultActions.andReturn().getResponse();
//        ReplyList replyList = objectMapper.readValue(response.getContentAsString(), ReplyList.class);
//
//        assertThat(replyList.getReplyNum()).isEqualTo(26);
    }

    @DisplayName("댓글 수정 API - 성공")
    @WithAccount("joohyuk")
    @Test
    void modifyReply_success() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").get();
        Board board = boardFactory.createBoard(account);
        ReplyCreateForm replyCreateform = ReplyCreateForm.builder()
                .content("test reply")
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
        Reply reply = replyFactory.createReply(account, board, replyCreateform);

        ReplyUpdateForm replyUpdateForm = ReplyUpdateForm.builder()
                .content("modify test reply")
//                .modifiedDateTime(LocalDateTime.now())
                .build();

        String requestUrl = "/boards/" + board.getId() + "/replies/" + reply.getId();

        // when
        ResultActions resultActions = mockMvc.perform(put(requestUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(replyUpdateForm))
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();

        ReplyReadForm replyReadForm = objectMapper.readValue(contentAsString, ReplyReadForm.class);

        assertThat(replyReadForm.getContent()).isEqualTo("modify test reply");
        assertThat(replyReadForm.getBoardId()).isEqualTo(board.getId());
        assertThat(replyReadForm.getNickname()).isEqualTo(account.getNickname());
    }

    @DisplayName("댓글 수정 API - 실패(내용 입력 x)")
    @WithAccount("joohyuk")
    @Test
    void modifyReply_fail() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").get();
        Board board = boardFactory.createBoard(account);
        ReplyCreateForm replyCreateform = ReplyCreateForm.builder()
                .content("test reply")
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
        Reply reply = replyFactory.createReply(account, board, replyCreateform);

        ReplyUpdateForm replyUpdateForm = ReplyUpdateForm.builder()
                .content("")
//                .modifiedDateTime(LocalDateTime.now())
                .build();

        String requestUrl = "/boards/" + board.getId() + "/replies/" + reply.getId();

        // when
        ResultActions resultActions = mockMvc.perform(put(requestUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(replyUpdateForm))
                .with(csrf()));

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists());

    }

    @DisplayName("댓글 삭제")
    @WithAccount("joohyuk")
    @Test
    void removeReply() throws Exception {
        // given
        Account account = accountRepository.findByNickname("joohyuk").get();
        Board board = boardFactory.createBoard(account);
        ReplyCreateForm replyCreateform = ReplyCreateForm.builder()
                .content("test reply")
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();
        Reply reply = replyFactory.createReply(account, board, replyCreateform);

        // when
        String requestUrl = "/boards/" + board.getId() + "/replies/" + reply.getId();
        ResultActions resultActions = mockMvc
                .perform(delete(requestUrl)
                .with(csrf()))
                .andDo(print());

        // then
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        ReplyReadForm replyReadForm = objectMapper.readValue(response.getContentAsString(), ReplyReadForm.class);

        Reply resultReply = replyRepository.findById(replyReadForm.getBoardId()).orElse(null);
        assertThat(resultReply).isEqualTo(null);
    }
}