package com.epam.esm.model;

import com.epam.esm.audit.EntityActionListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(EntityActionListener.class)
public class User implements Model, UserDetails {
    private static final int HASH_CODE = 19;
    @Id
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private Long id;
    @Column(name = "login", length = 64, unique = true, nullable = false)
    private String login;
    @Column(name = "name", length = 64)
    private String name;
    @Column(name = "password", length = 64)
    private String password;
    @Builder.Default
    @Column(name = "is_account_non_expired", columnDefinition = "boolean default true")
    private boolean isAccountNonExpired = true;
    @Builder.Default
    @Column(name = "is_account_non_locked", columnDefinition = "boolean default true")
    private boolean isAccountNonLocked = true;
    @Builder.Default
    @Column(name = "is_credentials_non_expired", columnDefinition = "boolean default true")
    private boolean isCredentialsNonExpired = true;
    @Builder.Default
    @Column(name = "is_enabled", columnDefinition = "boolean default true")
    private boolean isEnabled = true;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(32) default 'USER'")
    private UserRole role;
    @Column(name = "removed", columnDefinition = "boolean default false")
    private boolean removed;
    @Builder.Default
    @Column(name = "authentication_provider", nullable = false)
    private String authenticationProvider = "local";

    public User(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public Set<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return HASH_CODE;
    }
}
