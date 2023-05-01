package com.pseong.spring3template.security;

import com.pseong.spring3template.domain.User;
import com.pseong.spring3template.src.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String sub)
            throws UsernameNotFoundException {

        User user = userRepository.findBySub(sub);
        if (user == null) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }
        return new SecurityUser(user);
    }
    public UserDetails loadUserByUserid(Long userId)
            throws UsernameNotFoundException {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저가 존재하지 않습니다.");
        }
        return new SecurityUser(user.get());
    }
}