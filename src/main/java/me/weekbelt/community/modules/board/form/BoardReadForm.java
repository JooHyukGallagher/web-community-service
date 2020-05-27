package me.weekbelt.community.modules.board.form;

import lombok.*;
import me.weekbelt.community.modules.board.BoardType;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class BoardReadForm {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdDateTime;

    private Integer viewCount;

    private BoardType boardType;

    private String nickname;
}
