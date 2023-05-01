package com.pseong.spring3template.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 프로젝트에서 공통적으로 사용하는 상수들
public class Constant {
    public static final String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/tokeninfo";
    public static final String[][] WHITE_LIST_URI_EQUAL = {
            {"/user/login", "POST"},
            {"/test/login", "GET"}
    };
    public static final String[][] WHITE_LIST_URI_CONTAIN = {
            {"/swagger-ui", "ANY"},
            {"/swagger-resources", "ANY"},
            {"/v3/api-docs", "ANY"}
    };
}

