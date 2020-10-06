package me.weekbelt.community.modules.board.form;

import lombok.*;
import me.weekbelt.community.modules.board.BoardType;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class BoardUpdateForm {

    @NotBlank
    @Length(min = 2, max = 30)
    private String title;

    @NotBlank
    @Length(min = 10, message = "10자 이상 입력해 주세요.")
    private String content;

    private BoardType boardType;
}
