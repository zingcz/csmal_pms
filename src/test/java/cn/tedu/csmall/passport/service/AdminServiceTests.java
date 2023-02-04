package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AdminServiceTests {

    @Autowired
    IAdminService service;

    @Test
    void addNew() {
        AdminAddNewDTO admin = new AdminAddNewDTO();
        admin.setUsername("管理员100");
        admin.setPassword("123456");
        admin.setPhone("13900139100");
        admin.setEmail("13900139100@baidu.com");

        try {
            service.addNew(admin);
            log.debug("添加数据完成！");
        } catch (ServiceException e) {
            log.debug("添加数据失败！名称已经被占用！");
        }
    }

}
