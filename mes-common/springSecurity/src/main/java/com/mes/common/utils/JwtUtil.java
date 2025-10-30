package com.mes.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

import java.util.Date;

/**
 * @Author : zhoush
 * @Date: 2024/4/9 19:15
 * @Description:
 */
@Data
public class JwtUtil {

    private static final long EXPIRE = 60 * 60 * 1000L;
    private static final String SECRET = "beibo";
    private static final String HEADER = "Authorization";

    /*
     *生成jwt
     *
     * @param username
     */
    public static String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * EXPIRE);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                // 7天過期
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /*
     *解析jwt
     *
     * @param jwt
     *
     * @param secret
     */
    public static Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /*
     *jwt是否过期
     *
     * @param claims
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

}
