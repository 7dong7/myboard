package org.myboard.studyboard.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myboard.studyboard.domain.board.dto.BoardRequestDTO;
import org.myboard.studyboard.domain.board.dto.BoardResponseDTO;
import org.myboard.studyboard.domain.board.entity.BoardEntity;
import org.myboard.studyboard.domain.board.repository.BoardRepository;
import org.myboard.studyboard.domain.user.entity.UserEntity;
import org.myboard.studyboard.domain.user.entity.UserRoleType;
import org.myboard.studyboard.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 유저 접근 권한 체크
    public Boolean isAccess(Long id) { // 게시글 번호를 받는다
        
        // 현재 로그인되어 있는 유저의 이름
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // 현재 로그인 되어 있는 유저의 role
        String sessionRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();


        // 수직적으로 ADMIN 이면 모두 허용
        if ("ROLE_ADMIN".equals(sessionRole)) {
            return true;
        }

        // 특정 게시글 ID에 대해 본인이 작성 했는지 확인
        String boardUsername = boardRepository.findById(id).orElseThrow().getUserEntity().getUsername();
        if (sessionUsername.equals(boardUsername)) {
            return true;
        }
        
        // 나머지 다 불가
        return false;
    }
    
    
    
    // 새로운 게시글을 작성
    @Transactional
    public void createOneBoard(BoardRequestDTO dto) {

        // 게시글 dto -> entity
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(dto.getTitle());
        boardEntity.setContent(dto.getContent());
        
        // entity 저장
        boardRepository.save(boardEntity);

        //
        // UserEntity 와 BoardEntity 연결
        
        // 현재 게시글을 작성하는 유저
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 해당 유저의 Entity 가져오기
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow();

        // 연관 관계 만드는 메소드 호출
        userEntity.addBoard(boardEntity);
    }

    // 존재하는 특정글 조회
    public BoardResponseDTO readOneBoard(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow();

        // BoardEntity -> dto
        BoardResponseDTO dto = new BoardResponseDTO();
        dto.setId(boardEntity.getId());
        dto.setTitle(boardEntity.getTitle());
        dto.setContent(boardEntity.getContent());

        return dto;
    }

    // 존재하는 게시글 조회 (전부)
    public List<BoardResponseDTO> readAllBoards() {

        List<BoardEntity> list = boardRepository.findAll();

        List<BoardResponseDTO> dtos = new ArrayList<>();

        for (BoardEntity boardEntity : list) {
            BoardResponseDTO dto = new BoardResponseDTO();

            dto.setId(boardEntity.getId());
            dto.setTitle(boardEntity.getTitle());
            dto.setContent(boardEntity.getContent());

            dtos.add(dto);
        }
        return dtos;
    }
    
    // 특정 게시글을 수정
    @Transactional
    public void updateOneBoard(Long id, BoardRequestDTO dto) {
        
        // 기존의 ID 에 대한 게시글 데이터 불러오기
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow();

        // 게시글 dto -> entity
        boardEntity.setTitle(dto.getTitle());
        boardEntity.setContent(dto.getContent());
    }
    
    // 특정 게시글 삭제
    @Transactional
    public void deleteOneBoard(Long id) {
        boardRepository.deleteById(id);
    }
    
}
