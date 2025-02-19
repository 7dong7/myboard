package org.myboard.studyboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myboard.studyboard.domain.user.dto.UserRequestDTO;
import org.myboard.studyboard.domain.user.dto.UserResponseDTO;
import org.myboard.studyboard.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 회원 가입 : 페이지
    @GetMapping("/user/join")
    public String joinPage() {

        return "join";
    }

    // 회원 가입 : 수행
    @PostMapping("/user/join")
    public String joinProcess(@ModelAttribute UserRequestDTO dto) {

        userService.crateOneUser(dto);
        return "redirect:/login";
    }
    
    // 회원수장 : 페이지
    @GetMapping("/user/update/{username}")
    public String updatePage(@PathVariable("username") String username,
                             Model model) {

        // 본인 또는 ADMIN 만 접근 가능
        if(userService.isAccess(username)) {
            UserResponseDTO dto = userService.readOneUser(username);
            model.addAttribute("USER", dto);
            return "update";
        } else {
            return "redirect:/login";
        }
    }

    // 회원수정 : 수행
    @PostMapping("/user/update/{username}")
    public String updateProcess(@PathVariable("username") String username,
                                @ModelAttribute UserRequestDTO dto) {

        // 본인 또는 ADMIN 만 접근 가능
        if(userService.isAccess(username)) {
            userService.updateOneUser(dto, username);
            }

        return "redirect:/user/update/" + username;
    }


}
