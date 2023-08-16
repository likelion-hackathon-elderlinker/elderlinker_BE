package com.example.elderlinker.User;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(String userid, String password, String username, String phonenumber) {
        SiteUser user = new SiteUser();
        user.setUserID(userid);
        user.setPhoneNumber(phonenumber);
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
    }


    public void changePassword(String username, String newPassword) {
        SiteUser user = userRepository.findByUserID(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public SiteUser findByUserID(String username) {
        return userRepository.findByUserID(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public void changePhoneNumber(String username, String newPhoneNumber) {
        SiteUser user = userRepository.findByUserID(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.setPhoneNumber(newPhoneNumber);
        userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        return userRepository.findByUserID(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }


}
