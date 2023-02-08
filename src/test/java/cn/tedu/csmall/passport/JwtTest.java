package cn.tedu.csmall.passport;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JwtTest {
    String secretKey = "fejwfjiudqioadcmhureyt4euyuyuoh";
    @Test
    public void produceJWT(){
        Map claims = new HashMap();
        claims.put("username","test");
        claims.put("password","123");
        Date date = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
    }
}
