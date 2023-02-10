package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.security.LoginPrincipal;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.service.IRoleService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 处理管理员相关请求的控制器
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/roles")
@Api(tags = "1. 角色管理模块")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    // http://localhost:9081/roles/list
    @ApiOperation("查询角色列表")
    @ApiOperationSupport(order = 300)
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    @PostMapping("/list")
    public JsonResult<List<RoleListItemVO>> list(@AuthenticationPrincipal LoginPrincipal loginPrincipal) {
        log.debug("开始处理【查询角色】的请求)");
        List<RoleListItemVO> list = roleService.list();
        return JsonResult.ok(list);
    }

}
