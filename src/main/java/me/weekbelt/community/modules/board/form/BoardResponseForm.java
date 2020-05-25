package me.weekbelt.community.modules.board.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.weekbelt.community.modules.board.BoardType;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@Getter
public class BoardResponseForm {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdDateTime;

    private Integer viewCount;

    private BoardType boardType;
}
