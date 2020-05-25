package me.weekbelt.community.modules.board.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.weekbelt.community.modules.board.BoardType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@Getter @Setter
public class BoardRequestForm {

    @NotBlank
    @Length(min = 2, max = 30)
    private String title;

    @NotBlank
    @Length(min = 10)
    private String content;

    private BoardType boardType = BoardType.FREE;
}
