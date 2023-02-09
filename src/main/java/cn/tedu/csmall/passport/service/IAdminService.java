package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminUpdateDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 处理管理员数据的业务接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Transactional
public interface IAdminService {

    /**
     * 添加管理员
     *
     * @param adminAddNewDTO 管理员数据
     */
    void addNew(AdminAddNewDTO adminAddNewDTO);

    /**
     * 根据id删除管理员
     * @param id  管理员数据id
     */
    void deleteById(Long id);

    /**
     *
     * @param id 管理员数据id
     */
    void enable(Long id);

    /**
     *
     * @param id 管理员数据id
     */
    void disable(Long id);
    /**
     * 登录
     *
     * @param adminAddNewDTO 管理员数据
     */
    String login(AdminLoginDTO adminAddNewDTO);
    /**
     * 查询管理员列表
     *
     *
     */
    List<AdminListItemVO> list();

}
