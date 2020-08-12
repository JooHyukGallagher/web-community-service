package me.weekbelt.community.infra.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.community.modules.board.Board;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class FileInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String fileName;

    private String saveFileName;

    private String contentType;

    private boolean deleteFlag;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;
}
