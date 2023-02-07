package cn.tedu.csmall.passport;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void test(){
        String password = "123";
//        for(int i = 0; i< 50 ; i++){
            String encodePassword = passwordEncoder.encode(password);
            System.out.println(encodePassword);
            System.out.println(passwordEncoder.matches(password,encodePassword));
//        }
    }
}
