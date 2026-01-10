package example.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_user")
public class User {

    @Id
    @SequenceGenerator(name = "t_user_user_id_seq", sequenceName = "t_user_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_user_user_id_seq")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "oauth_subject", nullable = false, length = 255, unique = true)
    private String oauthSubject;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    protected User() {
        // for JPA
    }

    public User(String oauthSubject) {
        this.oauthSubject = oauthSubject;
        this.creationDate = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public String getOauthSubject() {
        return oauthSubject;
    }

    public void setOauthSubject(String oauthSubject) {
        this.oauthSubject = oauthSubject;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
