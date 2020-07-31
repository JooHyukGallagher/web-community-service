package me.weekbelt.community.modules.reply.form;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ReplyReadForm {

    private Long boardId;

    private String nickname;

    private String content;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;
}
