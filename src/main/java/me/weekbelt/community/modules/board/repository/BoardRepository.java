package me.weekbelt.community.modules.board.repository;

import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom{

    Page<Board> findAllByBoardTypeOrderByIdDesc(BoardType boardType, Pageable pageable);

    Page<Board> findAllByOrderByIdDesc(Pageable pageable);

    @Query("select b from Board b inner join b.account a where b.id = :boardId")
    Optional<Board> findBoardWithAccountById(@Param("boardId") Long boardId);
}
