package org.myboard.studyboard.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myboard.studyboard.domain.user.dto.UserRequestDTO;
import org.myboard.studyboard.domain.user.dto.UserResponseDTO;
import org.myboard.studyboard.domain.user.entity.UserEntity;
import org.myboard.studyboard.domain.user.entity.UserRoleType;
import org.myboard.studyboard.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 유저 접근 제한 체크
    public boolean isAccess(String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 로그인되어 있는 사용자의 이름
        String sessionUsername = authentication.getName();
        // 현재 로그인되어 있느 사용자의 권한
        String sessionRole = authentication.getAuthorities().iterator().next().getAuthority();

        // 수직적으로 ADMIN 인 경우는 무조건 접근 가능
        if ("ROLE_ADMIN".equals(sessionRole)) {
            return true;
        }

        // 수평적으로 특정행위를 수행할 username 에 대해 (현재 로그인한) username 과 같은지 확인
        if (username.equals(sessionUsername)) {
            return true;
        }
        
        // 나머지 다 불가
        return false;
    }
    
    
    // 유저 회원 가입
    @Transactional
    public void crateOneUser(UserRequestDTO dto) {

        String username = dto.getUsername();
        String password = dto.getPassword();
        String nickname = dto.getNickname();
        
        // 동일한 username 을 가지는 사용자가 있는지 확인
        if(userRepository.existsByUsername(username)) {
            // 예외 메시지 처리 ....
            return;
        }

        // 유저에 대한 Entity 생성 ( dto -> entity 변환 )
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(UserRoleType.USER)
                .build();

        // entity -> DB 저장
        userRepository.save(userEntity);
    }

    // 유저 정보 조회 (단일)
    public UserResponseDTO readOneUser(String username) {
        // 유저 한 명을 username 을 사용해서 조회
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 존재하지 않습니다."));

        // entity -> dto 변환
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(userEntity.getUsername());
        dto.setNickname(userEntity.getNickname());
        dto.setRole(userEntity.getRole().name());

        return dto;
    }

    // 유저 정보 조회 (복수)
    public List<UserResponseDTO> readAllUser() {

        List<UserEntity> userEntities = userRepository.findAll();

        return userEntities.stream()
                .map(userEntity -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setUsername(userEntity.getUsername());
                    dto.setNickname(userEntity.getNickname());
                    dto.setRole(userEntity.getRole().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 로그인 처리를 위한 유저 정보 조회
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    // 회원 정보 수정
    @Transactional
    public void updateOneUser(UserRequestDTO dto, String username) {
        
        // 기존 유저 정보 조회
        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 존재하지 않습니다."));

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }

        if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            entity.setNickname(dto.getNickname());
        }
    }

    // 회원 정보 삭제
    @Transactional
    public void deleteOneUser(String username) {

        userRepository.deleteByUsername(username);
    }
    
}
