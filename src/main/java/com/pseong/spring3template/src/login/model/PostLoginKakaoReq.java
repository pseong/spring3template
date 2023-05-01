package com.pseong.spring3template.src.login.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLoginKakaoReq {
    private String token;
}
