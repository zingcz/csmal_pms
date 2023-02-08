package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.vo.AdminLoginVO;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 处理管理员数据的业务实现类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    public AdminServiceImpl() {
        log.debug("创建业务对象：AdminServiceImpl");
    }

//    @Override
//    public void login(AdminLoginDTO adminLoginDTO){
//        AdminLoginVO adminLoginVO = adminMapper.getStandardByName(adminLoginDTO.getUsername());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(adminLoginVO.getUsername(),adminLoginVO.getPassword());
//        authenticationManager.authenticate(authentication);
//    }
    @Override
    public void addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的业务，参数：{}", adminAddNewDTO);

        {
            // 从参数对象中取出用户名
            String username = adminAddNewDTO.getUsername();
            // 调用adminMapper.countByUsername()执行统计
            int count = adminMapper.countByUsername(username);
            // 判断统计结果是否大于0
            if (count > 0) {
                // 是：抛出异常（ERR_CONFLICT）
                String message = "添加管理员失败，尝试使用的用户名已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        {
            // 从参数对象中取出手机号码
            String phone = adminAddNewDTO.getPhone();
            // 调用adminMapper.countByPhone()执行统计
            int count = adminMapper.countByPhone(phone);
            // 判断统计结果是否大于0
            if (count > 0) {
                // 是：抛出异常（ERR_CONFLICT）
                String message = "添加管理员失败，尝试使用的手机号码已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        {
            // 从参数对象中取出电子邮箱
            String email = adminAddNewDTO.getEmail();
            // 调用adminMapper.countByEmail()执行统计
            int count = adminMapper.countByEmail(email);
            // 判断统计结果是否大于0
            if (count > 0) {
                // 是：抛出异常（ERR_CONFLICT）
                String message = "添加管理员失败，尝试使用的电子邮箱已经被占用！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        // 创建Admin对象
        Admin admin = new Admin();
        // 复制参数DTO对象中的属性到实体对象中
        BeanUtils.copyProperties(adminAddNewDTO, admin);
        // 设置初始登录次数
        admin.setLoginCount(0);
        // 调用adminMapper.insert()方法插入管理员数据
        int rows = adminMapper.insert(admin);
        if (rows != 1) {
            String message = "添加管理员失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }

}
