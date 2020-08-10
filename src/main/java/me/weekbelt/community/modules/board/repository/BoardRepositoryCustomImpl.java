package me.weekbelt.community.modules.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.board.BoardType;
import me.weekbelt.community.modules.board.form.BoardSearch;
import me.weekbelt.community.modules.board.form.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static me.weekbelt.community.modules.board.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Board> findByBoardSearch(BoardSearch boardSearch, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (boardSearch.getSearchCondition() != null && isTotalSearch(boardSearch)) {
            builder.and(board.title.containsIgnoreCase(boardSearch.getKeyword()));
            builder.or(board.account.nickname.containsIgnoreCase(boardSearch.getKeyword()));
        } else if (boardSearch.getSearchCondition() != null && isTitleSearch(boardSearch)) {
            builder.and(board.title.containsIgnoreCase(boardSearch.getKeyword()));
        } else if (boardSearch.getSearchCondition() != null && isNicknameSearch(boardSearch)) {
            builder.and(board.account.nickname.containsIgnoreCase(boardSearch.getKeyword()));
        }

        if (!boardSearch.getBoardType().equals("ALL")) {
            builder.and(board.boardType.eq(BoardType.valueOf(boardSearch.getBoardType())));
        }

        QueryResults<Board> boardQueryResults = jpaQueryFactory
                .selectFrom(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(builder)
                .orderBy(board.id.desc())
                .fetchResults();

        List<Board> boardList = boardQueryResults.getResults();
        return new PageImpl<>(boardList, pageable, boardQueryResults.getTotal());
    }

    private boolean isTotalSearch(BoardSearch boardSearch) {
        return SearchCondition.valueOf(boardSearch.getSearchCondition()) == SearchCondition.TOTAL;
    }

    private boolean isTitleSearch(BoardSearch boardSearch) {
        return SearchCondition.valueOf(boardSearch.getSearchCondition()) == SearchCondition.TITLE;
    }
    private boolean isNicknameSearch(BoardSearch boardSearch) {
        return SearchCondition.valueOf(boardSearch.getSearchCondition()) == SearchCondition.NICKNAME;
    }
}
