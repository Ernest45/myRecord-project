package me.hanjun.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Table(name = "users")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname) {

        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }


    public User update(String nickname) {
        this.nickname = nickname;

        return this;
    }



    @Override
    public String getUsername() {
        return email;
        // 사용자의 id를 반환(고유한)
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        return true;
        // 만료 확인 로직 (true는 아직 만료 전)
    }

    @Override
    // 계정 잠금 여부 확인
    public boolean isAccountNonLocked() {
        return true;
        // 잠금 x 뜻
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        // 패스워드 만료 여부 반환 만료 x
    }

    @Override
    public boolean isEnabled() {
        return true;
        // 계정 사용 여부 반환 사용가능
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }
}
