package me.weekbelt.community.modules.reply.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ReplyCreateForm {

    @Length(min = 2, max = 1000)
    private String content;

    private String boardWriterNickname;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;
}
