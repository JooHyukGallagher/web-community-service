package me.weekbelt.community.modules.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @NoArgsConstructor @EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinAt;

    private String bio;
    private String occupation;
    private String location;
    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    public void generateEmailCheckToken(){
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
