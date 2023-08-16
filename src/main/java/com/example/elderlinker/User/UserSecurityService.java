package com.example.elderlinker.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자의 아이디(username)를 기반으로 데이터베이스에서 사용자 정보를 조회.
        Optional<SiteUser> _siteUser = this.userRepository.findByUserID(username);

        if (_siteUser.isEmpty()) {
            // 조회된 사용자 정보가 없으면 예외를 던져서 Spring Security가 처리.
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 조회된 사용자 정보를 가져옴.
        SiteUser siteUser = _siteUser.get();

        // 사용자의 권한 정보를 설정함. "admin"인 경우 ADMIN 권한을 부여하고 그 외에는 USER 권한을 부여.
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Userrole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Userrole.USER.getValue()));
        }

        // Spring Security에서 사용하는 UserDetails 객체로 변환하여 반환.
        return new User(siteUser.getUserID(), siteUser.getPassword(), authorities);
    }
}
