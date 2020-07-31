package me.weekbelt.community.modules.reply.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class ReplyList {

    private Page<ReplyReadForm> replyList;

    private Long replyNum;
}
