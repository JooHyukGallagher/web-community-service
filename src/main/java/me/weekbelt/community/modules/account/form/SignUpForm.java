package me.weekbelt.community.modules.account.form;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpForm {
//
//    @NotBlank
//    @Length(min = 3, max = 20)
//    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$")
    private String nickname;
//
//    @Email
//    @NotBlank
    private String email;
//
//    @NotBlank
//    @Length(min = 8, max = 50)
    private String password;
}
