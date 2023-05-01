package com.pseong.spring3template.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String sub;
    @Column(unique = true)
    private String name;
    private String roll;
    private LocalDateTime createDate;
    private Boolean active;
    public User() {

    }
}