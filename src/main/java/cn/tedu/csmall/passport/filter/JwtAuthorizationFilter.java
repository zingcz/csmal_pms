package cn.tedu.csmall.passport.filter;

import cn.tedu.csmall.passport.security.LoginPrincipal;
import cn.tedu.csmall.passport.web.JsonResult;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;


@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 清除SecurityContext中的数据
        SecurityContextHolder.clearContext();

        String jwt = request.getHeader("Authorization");
        log.debug("尝试接收客户端携带的JWT数据，JWT：{}", jwt);

        if (!StringUtils.hasText(jwt) || jwt.length() < 123){
            filterChain.doFilter(request, response);
            return;
        }

        //为下面反向拿去token数据准备
        //避免直传http消息报错 修改头部编码格式
        String secretKey = "dasdafgf8r7g48re74g8er94g89e4rg89e4r";
        Claims claims = null;
        response.setContentType("application/json; charset=utf-8");

        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException e) {
            String message = "您的登录信息已过期，请重新登录！";
            log.warn("解析JWT时出现ExpiredJwtException，响应消息：{}", message);
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_EXPIRED, message);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(JSON.toJSONString(jsonResult));
            printWriter.close();
            return;
        } catch (MalformedJwtException e) {
            String message = "非法访问！";
            log.warn("解析JWT时出现MalformedJwtException，响应消息：{}", message);
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, message);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(JSON.toJSONString(jsonResult));
            printWriter.close();
            return;
        } catch (SignatureException e) {
            String message = "非法访问！";
            log.warn("解析JWT时出现SignatureException，响应消息：{}", message);
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_SIGNATURE, message);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(JSON.toJSONString(jsonResult));
            printWriter.close();
        }

        //反向拿取token信息
        String username = claims.get("username",String.class);
        Long id = claims.get("id",Long.class);
        String authoritiesJsonString = claims.get("authoritiesJsonString", String.class);
        log.debug("从JWT中解析得到的管理员ID：{}", id);
        log.debug("从JWT中解析得到的管理员用户名：{}", username);
        log.debug("从JWT中解析得到的管理员权限列表JSON：{}", authoritiesJsonString);
        //转JSON为权限String
        Collection<SimpleGrantedAuthority> authorities = JSON.parseArray(authoritiesJsonString,SimpleGrantedAuthority.class);

        //准备新的代理人 类 的对象
        LoginPrincipal loginPrincipal = new LoginPrincipal();
        loginPrincipal.setId(id);
        loginPrincipal.setUsername(username);

        Object credentials = null;

        //传入上下文
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginPrincipal,credentials,authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        //放行
        filterChain.doFilter(request, response);
    }
}
