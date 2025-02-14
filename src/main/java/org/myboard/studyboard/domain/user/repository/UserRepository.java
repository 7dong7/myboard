package org.myboard.studyboard.domain.user.repository;

import org.myboard.studyboard.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 동일한 username 을 가지는 사용자가 있는지 확인
    boolean existsByUsername(String username);

    // 유저 한 명을 username 을 사용해서 조회
    Optional<UserEntity> findByUsername(String username);

    // 사용자 한명 삭제
    void deleteByUsername(String username);
}
