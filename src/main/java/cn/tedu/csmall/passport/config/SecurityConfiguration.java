package cn.tedu.csmall.passport.config;


import cn.tedu.csmall.passport.filter.JwtAuthorizationFilter;
import cn.tedu.csmall.passport.web.JsonResult;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 白名单
        // 使用1个星号，表示通配此层级的任意资源，例如：/admin/*，可以匹配 /admin/delete、/admin/add-new
        // 但是，不可以匹配多个层级，例如：/admin/*，不可以匹配 /admin/9527/delete
        // 使用2个连续的星号，表示通配任何层级的任意资源，例如：/admin/**，可以匹配 /admin/delete、/admin/9527/delete
        String[] urls = {
                "/doc.html",
                "/**/*.js",
                "/**/*.css",
                "/swagger-resources",
                "/v2/api-docs",
                "/admins/login"
        };

        // 禁用“防止伪造的跨域攻击”这种防御机制
        http.csrf().disable();
        http.cors();

        // 配置URL的访问控制
        // 注意：基于第一匹配原则，覆盖范围越大的匹配方法，应该配置在更靠后的代码位置
        http.authorizeRequests() // 配置URL的访问控制
                .mvcMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .mvcMatchers(urls) // 匹配某些URL
                .permitAll() // 直接许可，即：不需要通过认证就可以直接访问
                .anyRequest() // 任何请求（基于第一匹配原则，此处表示：除了以上配置过的以外的其它所有请求）
                .authenticated(); // 以上配置的请求需要是通过认证的

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);


        // 调用formLogin()表示启用登录表单页和登出页
        // 如果未调用此方法，则没有登录表单页和登出页，且，当视为“未通过认证时”，将响应403
//         http.formLogin();


        http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
                String message = "未检测到登录，请登录！（在开发阶段，看到此提示时，请检查客户端是否携带了JWT向服务器端发起请求）";
                JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED, message);
                response.setContentType("application/json; charset=utf-8");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(JSON.toJSONString(jsonResult));
                printWriter.close();
            }
        });
    }

}