package com.pseong.spring3template.test;

import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.config.BaseResponse;
import com.pseong.spring3template.reqreslog.NoLogging;
import com.pseong.spring3template.test.model.GetTestRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.pseong.spring3template.config.BaseResponseStatus.REQUEST_ERROR;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping ("name")
    public BaseResponse<GetTestRes> getName() {
        return new BaseResponse<>(new GetTestRes("SeongUk"));
    }

    @GetMapping ("error")
    public BaseResponse<GetTestRes> getError() {
        try {
            throw new BaseException(REQUEST_ERROR);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}