package me.weekbelt.community.modules.account;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;
    private String occupation;
    private String location;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    @Builder
    public Account(String email, String nickname, String password, boolean emailVerified,
                   LocalDateTime joinedAt) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.emailVerified = emailVerified;
        this.joinedAt = joinedAt;
    }
}
