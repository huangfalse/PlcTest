package com.mes.common.handler;

import cn.hutool.json.JSONUtil;
import com.mes.common.utils.JwtUtil;
import com.mes.result.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * LoginSuccessHandler
 *
 * @author zsh
 * @date 2025/07/04
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String HEADER = "Authorization";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        // 生成jwt，并放置到请求头中
        String jwt = JwtUtil.generateToken(authentication.getName());
        response.setHeader(HEADER, jwt);

        Result result = Result.success("");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        outputStream.flush();
        outputStream.close();
    }

}
