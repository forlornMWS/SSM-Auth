package xyz.mwszksnmdys.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.mwszksnmdys.result.Result;
import xyz.mwszksnmdys.service.SysMenuService;
import xyz.mwszksnmdys.system.SysMenu;
import xyz.mwszksnmdys.vo.AssginMenuVo;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author mws
 * @since 2022-11-07
 */
@Api(tags="菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;



    // 菜单列表方法
    @ApiOperation("菜单列表")
    @GetMapping("/findNodes")
    public Result findNodes(){
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    // 添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){
        boolean isSucceed = sysMenuService.save(sysMenu);
        if(isSucceed){
            return Result.ok();
        }else {
            return  Result.fail();
        }
    }

    // 根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("/findNode/{id}")
    public Result findNode(@PathVariable String id){
        SysMenu sysMenu = sysMenuService.getById(id);
        return  Result.ok(sysMenu);
    }

    // 修改菜单
    @ApiOperation("修改菜单")
    @PostMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu){
        boolean isSucceed = sysMenuService.updateById(sysMenu);
        if(isSucceed){
            return Result.ok();
        }else {
            return  Result.fail();
        }
    }

    // 删除
    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id){
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }

    // 根据角色分配菜单
    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo) {
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }

}

