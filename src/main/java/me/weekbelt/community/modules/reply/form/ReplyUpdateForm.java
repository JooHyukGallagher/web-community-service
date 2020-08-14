package me.weekbelt.community.modules.reply.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ReplyUpdateForm {

    @Length(min = 2, max = 1000)
    private String content;
}
