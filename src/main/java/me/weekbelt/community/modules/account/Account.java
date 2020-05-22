package me.weekbelt.community.modules.account;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.community.modules.account.form.Profile;
import org.apache.tomcat.jni.Local;

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
    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    private String bio;
    private String occupation;
    private String location;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
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

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(10));
    }

    public void updateProfile(Profile profile) {
        this.occupation = profile.getOccupation();
        this.bio = profile.getBio();
        this.location = profile.getLocation();
    }
}
