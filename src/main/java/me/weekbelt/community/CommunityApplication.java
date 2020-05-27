package me.weekbelt.community;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.account.repository.AccountRepository;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.stream.IntStream;


@EnableJpaAuditing
@SpringBootApplication
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(BoardRepository boardRepository, AccountRepository accountRepository) {
        Account account = accountRepository.findByNickname("weekbelt")
                .orElseThrow(() -> new IllegalArgumentException(("찾는 계정이 없습니다.")));
        return (args -> {
            IntStream.rangeClosed(1,12).forEach(index -> {
                boardRepository.save(
                        Board.builder()
                                .title("공지 게시글" + index)
                                .content("공지 내용" + index)
                                .boardType(BoardType.NOTICE)
                                .createdDateTime(LocalDateTime.now())
                                .modifiedDateTime(LocalDateTime.now())
                                .account(account)
                                .build()
                );
            });
            IntStream.rangeClosed(1,56).forEach(index -> {
                boardRepository.save(
                        Board.builder()
                                .title("자유 게시글" + index)
                                .content("자유 내용" + index)
                                .boardType(BoardType.FREE)
                                .createdDateTime(LocalDateTime.now())
                                .modifiedDateTime(LocalDateTime.now())
                                .account(account)
                                .build()
                );
            });
            IntStream.rangeClosed(1,71).forEach(index -> {
                boardRepository.save(
                        Board.builder()
                                .title("질문 게시글" + index)
                                .content("질문 내용" + index)
                                .boardType(BoardType.QUESTION)
                                .createdDateTime(LocalDateTime.now())
                                .modifiedDateTime(LocalDateTime.now())
                                .account(account)
                                .build()
                );
            });
            IntStream.rangeClosed(1,26).forEach(index -> {
                boardRepository.save(
                        Board.builder()
                                .title("홍보 게시글" + index)
                                .content("홍보 내용" + index)
                                .boardType(BoardType.PROMOTION)
                                .createdDateTime(LocalDateTime.now())
                                .modifiedDateTime(LocalDateTime.now())
                                .account(account)
                                .build()
                );
            });
        });
    }
}
