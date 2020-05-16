package me.weekbelt.community.modules.board;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.community.modules.account.Account;

import javax.persistence.*;

@Getter @NoArgsConstructor @EqualsAndHashCode(of = "id")
@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private Integer viewCount = 0;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Builder
    public Board(String title, String content, Account account, Integer viewCount, BoardType boardType) {
        this.title = title;
        this.content = content;
        this.account = account;
        this.viewCount = viewCount;
        this.boardType = boardType;
    }
}
