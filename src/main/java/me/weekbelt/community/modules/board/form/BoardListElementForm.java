package me.weekbelt.community.modules.board.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardListElementForm {

    private Long id;

    private String title;

    private String nickname;

    private LocalDateTime createdDateTime;

    private Integer viewCount;

}
