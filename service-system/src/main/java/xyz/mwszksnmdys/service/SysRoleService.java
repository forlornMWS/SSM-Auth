package xyz.mwszksnmdys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.mwszksnmdys.system.SysRole;
import xyz.mwszksnmdys.vo.AssginRoleVo;
import xyz.mwszksnmdys.vo.SysRoleQueryVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    // 条件分页查询
    IPage<SysRole>  selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo);

    Map<String, Object> getRoleByUserId(String userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
