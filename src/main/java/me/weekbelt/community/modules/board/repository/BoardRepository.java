package me.weekbelt.community.modules.board.repository;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByBoardTypeOrderByIdDesc(BoardType boardType, Pageable pageable);

    Page<Board> findAllByOrderByIdDesc(Pageable pageable);

    Optional<Board> findByAccount(Account account);
}
