package me.weekbelt.community.modules.account.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class PasswordForm {

    @Length(min = 8, max = 50)
    private String newPassword;

    @Length(min = 8, max = 50)
    private String newPasswordConfirm;
}
