package me.weekbelt.community.modules.reply.form;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ReplyUpdateForm {

    private String content;

    private LocalDateTime modifiedDateTime;
}
