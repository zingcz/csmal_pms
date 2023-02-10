package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListItemVO;
import cn.tedu.csmall.passport.service.IAdminService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 处理管理员相关请求的控制器
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/admins")
@Api(tags = "1. 管理员管理模块")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @PostMapping("/login")
    @ApiOperation("管理员登录")
    public JsonResult<String> login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的请求，参数：{}", adminLoginDTO);
        String jwt = adminService.login(adminLoginDTO);
        return JsonResult.ok(jwt);
    }

    public AdminController() {
        log.debug("创建控制器对象：AdminController");
    }

    // http://localhost:9081/admins/add-new
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PostMapping("/add-new")
    @ApiImplicitParam(name = "roleIds", value = "角色ID", required = true, example = "1", dataType = "long")
    public JsonResult<Void> addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的请求，参数：{}", adminAddNewDTO);
        adminService.addNew(adminAddNewDTO);
        return JsonResult.ok();
    }

    // http://localhost:9081/admins/list
    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 300)
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    @PostMapping("/list")
    public JsonResult<List<AdminListItemVO>> list(@AuthenticationPrincipal UserDetails userDetails) {
        log.debug("开始处理【查询管理员】的请求)");
        List<AdminListItemVO> list = adminService.list();
        return JsonResult.ok(list);
    }

    // http://localhost:4090/admins/9527/enable
    @PostMapping("/{id:[0-9]+}/enable")
    @ApiOperation("启用类别")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id", value = "类别ID", required = true, example = "9527", dataType = "long")
    public JsonResult<Void> setEnable(@Range(min = 1, max = 1000000, message = "启用类别失败，类别ID非法！")
                                      @PathVariable Long id) {
        log.debug("开始处理【启用类别】的请求，参数：{}", id);
        adminService.enable(id);

        return JsonResult.ok();
    }

    // http://localhost:4090/admins/9527/disable
    @PostMapping("/{id:[0-9]+}/disable")
    @ApiOperation("禁用类别")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParam(name = "id", value = "类别ID", required = true, example = "9527", dataType = "long")
    public JsonResult<Void> setDisable(@Range(min = 1, max = 1000000, message = "禁用类别失败，类别ID非法！")
                                       @PathVariable Long id) {
        log.debug("开始处理【禁用类别】的请求，参数：{}", id);
        adminService.disable(id);
        return JsonResult.ok();
    }

    // http://localhost:4090/admins/9527/delete
    @PostMapping("/{id:[0-9]+}/delete")
    @ApiOperation("根据id删除类别")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id", value = "类别ID", required = true, example = "9527", dataType = "long")
    public JsonResult<Void> delete(@Range(min = 1, max = 1000000, message = "删除类别失败，类别ID非法！")
                                   @PathVariable Long id) {
        log.debug("开始处理【根据id删除类别】的请求，参数：{}", id);
        adminService.deleteById(id);
        return JsonResult.ok();
    }

}
