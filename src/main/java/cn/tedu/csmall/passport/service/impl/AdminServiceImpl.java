package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.mapper.AdminRoleMapper;
import cn.tedu.csmall.passport.mapper.RoleMapper;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.security.AdminDetails;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

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
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AdminServiceImpl() {
        log.debug("创建业务对象：AdminServiceImpl");
    }

    @Override
    public String login(AdminLoginDTO adminLoginDTO){
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminLoginDTO.getUsername(),adminLoginDTO.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authentication);
        log.debug("认证通过，认证结果：{}", authenticationResult);
        log.debug("认证通过，认证结果中的当事人：{}", authenticationResult.getPrincipal());

        Date date = new Date(System.currentTimeMillis() + 30 * 24 * 60 * 1000);
        String secretKey = "dasdafgf8r7g48re74g8er94g89e4rg89e4r";

        Map<String,Object> claims = new HashMap<>();
        AdminDetails principal = (AdminDetails) authenticationResult.getPrincipal();
        //拿取权限集合转为JSON字符
        Collection<GrantedAuthority> authorities = principal.getAuthorities();
        String authoritiesJsonString = JSON.toJSONString(authorities);
        System.out.println(authoritiesJsonString);

        claims.put("username",principal.getUsername());
        claims.put("id",principal.getId());
        claims.put("authoritiesJsonString",authoritiesJsonString);

        String jwt =  Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        return jwt;
    }
    @Override
    public void addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的业务，参数：{}", adminAddNewDTO);

        //不能添加角色1
        Long[] ids = adminAddNewDTO.getRoleIds();
        for(int i = 0; i < ids.length; i++){
            if(ids[i] == 1){
                String message = "添加管理员失败，非法访问！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

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
        // 处理密码加密
        String rawPassword = admin.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        admin.setPassword(encodedPassword);

        // 调用adminMapper.insert()方法插入管理员数据
        int rows = adminMapper.insert(admin);
        if (rows != 1) {
            String message = "添加管理员失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }

        //根据添加的角色id新建角色数组
        AdminRole[] adminRoleList = new AdminRole[ids.length];
        for(int i = 0; i < ids.length; i++){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getId());
            adminRole.setRoleId(ids[i]);
            adminRoleList[i] = adminRole;
        }

        //一致性检查
        int row = adminRoleMapper.insertBatch(adminRoleList);
        if(row != adminRoleList.length){
            String message = "添加管理员失败，服务器忙，请稍后再尝试！";
            throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
        }
    }

    @Override
    public List<AdminListItemVO> list(){
        return adminMapper.list();
    }

    @Override
    public void deleteById(Long id){
        AdminStandardVO admin = adminMapper.getStandardById(id);
        if( admin == null || id == 1){
            String message = "数据不存在";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        int count = adminMapper.deleteById(id);
        if(count != 1){
            String message = "操作失败服务器忙";
            log.debug("操作失败服务器忙");
            throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
        }

        int rows = adminRoleMapper.deleteByAdminId(id);
        if(rows < 1){
            String message = "删除管理员失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }

        log.debug("删除成功");
    }

    @Override
    public void enable(Long id) {
        updateEnableById(id, 1);
    }

    @Override
    public void disable(Long id) {
        updateEnableById(id, 0);
    }

        public void updateEnableById (Long id,Integer enable){
            String states[] = {"禁用", "启用"};


            AdminStandardVO adminStandardVO = adminMapper.getStandardById(id);
            if (adminStandardVO == null || id == 1) {
                String message = "数据不存在";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
            }

            if (adminStandardVO.getEnable().equals(enable)) {
                String message = "操作失败，管理员已经是" + states[enable]+"状态";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }

            Admin admin = new Admin();
            admin.setId(id);
            admin.setEnable(enable);
            int count = adminMapper.update(admin);
            if (count != 1) {
                String message = "操作失败服务器忙";
                log.debug("操作失败服务器忙");
                throw new ServiceException(ServiceCode.ERR_UPDATE, message);
            }
            log.debug("启用成功");
        }
}

