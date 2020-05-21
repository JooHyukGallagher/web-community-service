package me.weekbelt.community.modules.account.repository;

import me.weekbelt.community.modules.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
