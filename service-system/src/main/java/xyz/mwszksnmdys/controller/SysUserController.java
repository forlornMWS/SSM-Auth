package xyz.mwszksnmdys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.mwszksnmdys.result.Result;
import xyz.mwszksnmdys.service.SysUserService;
import xyz.mwszksnmdys.system.SysUser;
import xyz.mwszksnmdys.utils.MD5;
import xyz.mwszksnmdys.vo.SysUserQueryVo;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author mws
 * @since 2022-11-06
 */
@Api(tags = "用户管理接口 ")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;


    @ApiOperation("更改用户状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,
                               @PathVariable Integer status)
    {
        sysUserService.updateStatus(id, status);
        return Result.ok();
    }


    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "userQueryVo", value = "查询对象", required = false)
            SysUserQueryVo userQueryVo) {
        Page<SysUser> pageParam = new Page<>(page, limit);
        IPage<SysUser> pageModel = sysUserService.selectPage(pageParam, userQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser) {
        String password = sysUser.getPassword();
//        sysUser.setPassword(MD5Encoder.encode(password.getBytes()).toString());
        sysUser.setPassword(MD5.encrypt(password));
        boolean isSucceed = sysUserService.save(sysUser);
        if (isSucceed) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询")
    @GetMapping("/getUser/{id}")
    public Result getUserById(@PathVariable String id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation("修改用户")
    @PostMapping("/update")
    public Result update(@RequestBody SysUser sysUser) {
        boolean isSucceed = sysUserService.updateById(sysUser);
        if (isSucceed) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        boolean isSucceed = sysUserService.removeById(id);
        if (isSucceed) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
}

