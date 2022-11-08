package xyz.mwszksnmdys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xyz.mwszksnmdys.annotation.Log;
import xyz.mwszksnmdys.enums.BusinessType;
import xyz.mwszksnmdys.result.Result;
import xyz.mwszksnmdys.service.SysRoleService;
import xyz.mwszksnmdys.system.SysRole;
import xyz.mwszksnmdys.vo.AssginRoleVo;
import xyz.mwszksnmdys.vo.SysRoleQueryVo;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "查询所有角色")
    @GetMapping("/findAll")
    public Result findAll() {
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据id删除")
    @DeleteMapping("/remove/{id}")
    public Result removeRole(@PathVariable("id") String id) {
        // TODO 模拟异常
       /* try{
            int i = 10 / 0;
        }catch (Exception e)
        {
            throw new MWSException(2001, "自定义异常");
        }*/
        boolean isSuccess = sysRoleService.removeById(id);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("/{page}/{limit}")  // page表示当前页， limit表示每页显示的数量
    public Result findPageQueryRole(@PathVariable("page") Long page,
                                    @PathVariable("limit") Long limit,
                                    SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> pageParam = new Page<>(page, limit);
        IPage<SysRole> pageModel = sysRoleService.selectPage(pageParam, sysRoleQueryVo);
        return Result.ok(pageModel);
    }

    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result Save(@RequestBody SysRole sysRole) {
        boolean isSaved = sysRoleService.save(sysRole);
        return isSaved ? Result.ok() : Result.fail();
    }

    // 修改-根据id查询
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据id查询")
    @PostMapping("/findRoleById/{id}")
    public Result findRoleById(@PathVariable("id") String id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    // 修改-最终修改
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("最终修改")
    @PostMapping("/update")
    public Result updateRole(@RequestBody SysRole sysRole) {
        boolean isSucceed = sysRoleService.updateById(sysRole);
        if (isSucceed) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //    json数组格式 <-----> java的list集合
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<String> ids) {
        boolean isSucceed = sysRoleService.removeByIds(ids);
        if (isSucceed) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("获取用户角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable String userId) {
        Map<String, Object> roleMap = sysRoleService.getRoleByUserId(userId);
        return Result.ok(roleMap);
    }

    @ApiOperation("用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }
}
