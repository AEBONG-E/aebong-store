package com.aebong.store.common.security.userdetails;

import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. account=" + account));
        return new CustomUserDetails(userEntity);
    }
}
