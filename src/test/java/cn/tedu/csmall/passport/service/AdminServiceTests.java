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
        Long[] ids = new Long[]{2L};
        admin.setRoleIds(ids);
        try {
            service.addNew(admin);
            log.debug("添加数据完成！");
        } catch (ServiceException e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    void list(){
        List<AdminListItemVO>list = service.list();
        for(Object item: list){
            System.out.println(item.toString());
        }
    }

    @Test
    void delete(){
        try {
            service.deleteById(1L);
        } catch (ServiceException e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    void setEnable() {
        Long id = 5L;

        try {
            service.enable(id);
            log.debug("启用类别成功！");
        } catch (ServiceException e) {
            log.debug("捕获到异常，其中的消息：{}", e.getMessage());
        }
    }

    @Test
    void setDisable() {
        Long id = 5L;

        try {
            service.disable(id);
            log.debug("禁用类别成功！");
        } catch (ServiceException e) {
            log.debug("捕获到异常，其中的消息：{}", e.getMessage());
        }
    }

}
