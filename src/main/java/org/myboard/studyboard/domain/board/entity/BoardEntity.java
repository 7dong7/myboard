package org.myboard.studyboard.domain.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myboard.studyboard.domain.user.entity.UserEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

}
