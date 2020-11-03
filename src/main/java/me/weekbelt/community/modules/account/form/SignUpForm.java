package me.weekbelt.community.modules.account.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter @Setter
public class SignUpForm {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$",
            message = "닉네임 양식이 맞지 않습니다. 닉네임은 3-20자 사이로 입력해주세요.")
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;
}
