package cn.tedu.csmall.passport.security;

import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.pojo.vo.AdminLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Spring Security调用了loadUserByUsername()方法，参数：{}", s);

         AdminLoginVO loginInfo = adminMapper.getLoginInfoByUsername(s);
        log.debug("从数据库查询用户名【{}】匹配的信息，结果：{}", s, loginInfo);

        if (loginInfo == null) {
            return null; // 暂时
        }

        List<String> permissions = loginInfo.getPermissions();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(String permission: permissions){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
            authorities.add(authority);
        }

        AdminDetails adminDetails = new AdminDetails(loginInfo.getId(),
                loginInfo.getUsername(),
                loginInfo.getPassword(),
                loginInfo.getEnable() == 1,
                authorities);
        log.debug("即将向Spring Security返回UserDetails对象：{}", adminDetails);
        return adminDetails;
        //这是一个假人
//        UserDetails userDetails = User.builder()
//                .username(loginInfo.getUsername())
//                .password(loginInfo.getPassword())
//                .disabled(loginInfo.getEnable() == 0)
//                .accountLocked(false)
//                .accountExpired(false)
//                .credentialsExpired(false)
//                .authorities("暂时给个山寨权限，暂时没有作用，只是避免报错而已")
//                .build();
    }
}
