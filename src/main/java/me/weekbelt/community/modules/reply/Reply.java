package me.weekbelt.community.modules.reply;

import lombok.*;
import me.weekbelt.community.modules.account.Account;
import me.weekbelt.community.modules.board.Board;
import me.weekbelt.community.modules.reply.form.ReplyUpdateForm;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(of = "id")
@Entity
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    public void addBoard(Board board) {
        this.board = board;
        board.getReplyList().add(this);
    }

    public void update(ReplyUpdateForm replyUpdateForm) {
        this.content = replyUpdateForm.getContent();
        this.modifiedDateTime = LocalDateTime.now();
    }
}
