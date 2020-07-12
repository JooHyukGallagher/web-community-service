package me.weekbelt.community.modules.account;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;

@Getter
public class UserAccount extends User {

    private final Account account;

    public UserAccount(Account account, Set<GrantedAuthority> grantedAuthorities) {
        super(account.getNickname(), account.getPassword(), grantedAuthorities);
        this.account = account;
    }
}
