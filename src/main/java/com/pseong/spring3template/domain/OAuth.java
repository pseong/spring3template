package com.pseong.spring3template.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"user_id", "type"}
                )
        }
)
public class OAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Long type; // 1: Google, 2: Apple, 3: Kakao
    private String sub;
}