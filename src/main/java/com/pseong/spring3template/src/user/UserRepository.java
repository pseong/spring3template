package com.pseong.spring3template.src.user;

import com.pseong.spring3template.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findBySub(String sub);
    User findByName(String name);
}
