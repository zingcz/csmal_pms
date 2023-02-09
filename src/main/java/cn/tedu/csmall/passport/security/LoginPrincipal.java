package cn.tedu.csmall.passport.security;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
public class LoginPrincipal implements Serializable {
    /**
     * 当事人ID
     */
    private Long id;

    /**
     * 当事人用户名
     */
    private String username;

}
