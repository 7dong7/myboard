package org.myboard.studyboard.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING) // enum 타입을 저장하면 0,1 과 같이 저장됨 String 으로 ADMIN, USER 로 저장하기 위해 사용
    private UserRoleType role;

    @Builder
    public UserEntity(String username, String password, String nickname, UserRoleType role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
    
}
