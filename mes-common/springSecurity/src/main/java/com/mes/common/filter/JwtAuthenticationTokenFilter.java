package com.mes.common.filter;

import com.mes.common.utils.JwtUtil;
import com.mes.common.utils.UserInfoUtils;
import com.mes.userinfo.entity.LoginUser;
import com.mes.userinfo.service.SysUserService;
import com.mes.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author : zhoush
 * @Date: 2024/4/10 9:42
 * @Description:
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private SysUserService sysUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return; // 此处加上return好处是后面结果返回的时候就不会再走一遍此过滤器的方法了
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.getClaimByToken(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisUtil.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            response.setHeader("token", "");
            throw new RuntimeException("用户未登录");
        }
        //将用户信息放入当前线程
        UserInfoUtils.set(loginUser.getUser());
        //存入SecurityContextHolder，以供后面的过滤器使用
        List<String> permissionKeyList = sysUserService.getUserAuthorityInfo(Long.parseLong(userid));
        List<GrantedAuthority> authorities = permissionKeyList.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}