package com.example.elderlinker.User;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile; // 이미지 업로드를 처리하는 핸들러를 추가한 것.
import java.util.UUID;
import java.io.IOException;


@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService; // UserService 의존성 주입

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userCreateForm", new UserCreateForm()); // 빈 UserCreateForm 객체를 모델에 추가하여 뷰로 전달
        return "signup_form"; // 회원가입 양식 뷰를 반환 꼭 가입창 폼 이름 이걸로 해주세요! 아니면 수정 해야합니다!
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            return "signup_form"; // 입력 데이터의 유효성 검사 오류가 있으면 다시 회원가입 양식 뷰로 이동
        }

        // 유효성 검사가 통과하면 회원 생성 서비스를 호출하고 생성된 정보로 사용자 생성

        try {userService.create(userCreateForm.getUserid(),
                userCreateForm.getPassword(), userCreateForm.getUsername(), userCreateForm.getPhonenumber());}
        catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/h2.console"; // 회원가입 후 돌아올 페이지 설정 필요!!!
    }

    @GetMapping("/login")
    public String login() {
        return "login_form"; //로그인 form 여기에 설정 필요!!!!!!!!!!!
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        EditProfileForm editProfileForm = new EditProfileForm();
        SiteUser user = userService.findByUserID(username);

        editProfileForm.setNewPhoneNumber(user.getPhoneNumber());
        editProfileForm.setNewPassword(user.getPassword());
        editProfileForm.setConfirmNewPassword(user.getPassword());

        model.addAttribute("editProfileForm", editProfileForm);
        return "mypage_edit";
    }

    @PostMapping("/edit")
    public String processEditProfilepublic(@ModelAttribute("editProfileForm") @Valid EditProfileForm editProfileForm,
                                                                     BindingResult bindingResult, Model model,
                                                                     @RequestParam("profileImage") MultipartFile profileImage) {
        if (bindingResult.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            SiteUser user = userService.findByUserID(username);
            model.addAttribute("user", user);
            return "mypage_edit";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 새 비밀번호 변경 관련 코드
        if (!editProfileForm.getNewPassword().isEmpty()) {
            if (!editProfileForm.getNewPassword().equals(editProfileForm.getConfirmNewPassword())) {
                bindingResult.rejectValue("confirmNewPassword", "passwordMismatch", "비밀번호가 일치하지 않습니다.");
                SiteUser user = userService.findByUserID(username);
                model.addAttribute("user", user);
                return "mypage_edit";
            }
            userService.changePassword(username, editProfileForm.getNewPassword());
        }

        // 새 전화번호 변경 관련 코드
        if (!editProfileForm.getNewPhoneNumber().isEmpty()) {
            userService.changePhoneNumber(username, editProfileForm.getNewPhoneNumber());
        }


        return "redirect:/user/edit";
    }

}
