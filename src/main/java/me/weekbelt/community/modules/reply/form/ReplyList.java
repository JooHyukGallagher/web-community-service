package me.weekbelt.community.modules.reply.form;

import lombok.*;
import org.springframework.data.domain.Page;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ReplyList {

    private Page<ReplyReadForm> replyList;

    private Long replyNum;
}
