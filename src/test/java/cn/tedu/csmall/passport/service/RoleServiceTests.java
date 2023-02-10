package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class RoleServiceTests {

    @Autowired
    IRoleService service;


    @Test
    void list(){
        List<?>list = service.list();
        for(Object item: list){
            System.out.println(item.toString());
        }
    }
}
