package xyz.mwszksnmdys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.mwszksnmdys.system.SysUser;
import xyz.mwszksnmdys.vo.SysUserQueryVo;

import java.util.Map;


/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author mws
 * @since 2022-11-06
 */
public interface SysUserService extends IService<SysUser> {

    // 用户列表
    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo sysUserQueryVo);

    // 更改用户状态
    void updateStatus(String id, Integer status);

    SysUser getUserInfoByUsername(String username);

    Map<String, Object> getUserInfo(String username);
}
