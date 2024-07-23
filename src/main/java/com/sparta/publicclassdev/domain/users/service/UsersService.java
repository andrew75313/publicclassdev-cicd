package com.sparta.publicclassdev.domain.users.service;


import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.community.entity.Communities;
import com.sparta.publicclassdev.domain.community.repository.CommunitiesRepository;
import com.sparta.publicclassdev.domain.users.dao.UserRedisDao;
import com.sparta.publicclassdev.domain.users.dto.LoginRequestDto;
import com.sparta.publicclassdev.domain.users.dto.LoginResponseDto;
import com.sparta.publicclassdev.domain.users.dto.ProfileRequestDto;
import com.sparta.publicclassdev.domain.users.dto.ProfileResponseDto;
import com.sparta.publicclassdev.domain.users.dto.SignupRequestDto;
import com.sparta.publicclassdev.domain.users.dto.SignupResponseDto;
import com.sparta.publicclassdev.domain.users.dto.UpdateProfileResponseDto;
import com.sparta.publicclassdev.domain.users.entity.RoleEnum;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.CacheNames;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import com.sparta.publicclassdev.global.security.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final CommunitiesRepository communitiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRedisDao redisDao;

    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;
    public SignupResponseDto createUser(SignupRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        RoleEnum role = RoleEnum.USER;
        if(requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(ErrorCode.INCORRECT_MANAGER_KEY);
            }
            role = RoleEnum.ADMIN;
        }
        if(usersRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.USER_NOT_UNIQUE);
        }
        if(usersRepository.findByName(requestDto.getName()).isPresent()) {
            throw new CustomException(ErrorCode.NAME_NOT_UNIQUE);
        }
        Users user = Users.builder()
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .password(password)
            .role(role)
            .build();
        usersRepository.save(user);
        return SignupResponseDto.builder()
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }
    @Cacheable(cacheNames = CacheNames.LOGINUSER, key = "'login'+ #p0.getEmail()", unless = "#result== null")
    public LoginResponseDto login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        Users user = usersRepository.findByEmail(email).orElseThrow(
            () -> new CustomException(ErrorCode.CHECK_EMAIL)
        );
        if((!passwordEncoder.matches(password, user.getPassword()))) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        if(user.getRole().equals(RoleEnum.WITHDRAW)) {
            throw new CustomException(ErrorCode.USER_WITHDRAW);
        }
        LoginResponseDto responseDto = new LoginResponseDto(jwtUtil.createAccessToken(user),
            jwtUtil.createRefreshToken(user));
        redisDao.setRefreshToken(email, responseDto.getRefreshToken(), jwtUtil.getREFRESHTOKEN_TIME());
        usersRepository.save(user);
        return responseDto;
    }

    public ProfileResponseDto getProfile(Users user) {
        List<Communities> recentCommunities = communitiesRepository.findPostByUserLimit5(user);
        List<CommunitiesResponseDto> recentResponseDto = recentCommunities.stream().map(
            (communities) -> new CommunitiesResponseDto(communities.getTitle(), communities.getContent(), communities.getCategory()))
            .toList();
        return new ProfileResponseDto(user, recentResponseDto);
    }

    public UpdateProfileResponseDto updateProfile(Users user, ProfileRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        user.updateUsers(requestDto.getName(), password, requestDto.getIntro());
        usersRepository.save(user);
        return new UpdateProfileResponseDto(user);
    }

    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(
            () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @CacheEvict(cacheNames = CacheNames.LOGINUSER, key = "'login'+#p1")
    @Transactional
    public void logout(String accessToken, String email) {
        String substringToken = jwtUtil.substringToken(accessToken);
        Long expiration = jwtUtil.getExpiration(substringToken);
        redisDao.setBlackList(substringToken, "logout", expiration);
        if (redisDao.hasKey(email)) {
            redisDao.deleteRefreshToken(email);
        } else {
            throw new CustomException(ErrorCode.USER_LOGOUT);
        }
    }
}
