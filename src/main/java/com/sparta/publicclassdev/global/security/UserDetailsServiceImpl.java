package com.sparta.publicclassdev.global.security;

import com.sparta.publicclassdev.domain.user.entity.RoleEnum;
import com.sparta.publicclassdev.domain.user.entity.Users;
import com.sparta.publicclassdev.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException("Not Found " + email));

        // 이미 탈퇴 처리된 회원일 경우
        if (user.getRole() == RoleEnum.WITHDRAW) {
            throw new UsernameNotFoundException("User has withdrawn " + email);
        }

        return new UserDetailsImpl(user);
    }
}
