package me.weekbelt.community.modules.reply.repository;

import me.weekbelt.community.modules.reply.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findByBoardId(Long boardId);
}
