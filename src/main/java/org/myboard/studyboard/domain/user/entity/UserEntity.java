package org.myboard.studyboard.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myboard.studyboard.domain.board.entity.BoardEntity;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardEntity> boardEntityList = new ArrayList<>();


    @Builder
    public UserEntity(String username, String password, String nickname, UserRoleType role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    // 유저에 대해 새로운 글을 추가할 때 : 추가할 글을 파라미터로 받아 연관관계 매핑
    public void addBoard(BoardEntity boardEntity) {
        this.boardEntityList.add(boardEntity);
        boardEntity.setUserEntity(this);
    }
    
    // 유저에 대해 기존 글을 삭제할 때 : 삭제할 글을 받아서 연관관계에서 뺌
    public void removeBoardEntity(BoardEntity entity) {
        entity.setUserEntity(null);
        this.boardEntityList.remove(entity);
    }
    
}
