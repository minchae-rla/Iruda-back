package com.example.iruda.Error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "401 Unauthorized - 인증이 필요합니다.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "403 Forbidden - 접근 권한이 없습니다.";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404 Not Found - 요청하신 페이지를 찾을 수 없습니다.";
            }
        }
        return "알 수 없는 에러가 발생했습니다.";
    }
}