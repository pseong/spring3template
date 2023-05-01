package com.pseong.spring3template.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    private Long type; // 1: Google, 2: Apple, 3: Kakao
    @NotNull
    private String sub;
}