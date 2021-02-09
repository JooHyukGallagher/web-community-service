package me.weekbelt.community.modules.reply.service;

import static org.junit.jupiter.api.Assertions.*;

import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import me.weekbelt.community.modules.reply.repository.ReplyRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @MockBean
    private ReplyService replyService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ReplyRepository replyRepository;
}