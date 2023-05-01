package com.pseong.spring3template.src.oauth;

import com.pseong.spring3template.domain.OAuth;
import com.pseong.spring3template.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthRepository extends JpaRepository<OAuth, Long> {
    OAuth findByTypeAndSub(Long type, String sub);
}
