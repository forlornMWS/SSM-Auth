package xyz.mwszksnmdys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mwszksnmdys.mapper.SysUserMapper;
import xyz.mwszksnmdys.service.SysMenuService;
import xyz.mwszksnmdys.service.SysUserService;
import xyz.mwszksnmdys.system.SysUser;
import xyz.mwszksnmdys.vo.RouterVo;
import xyz.mwszksnmdys.vo.SysUserQueryVo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mws
 * @since 2022-11-06
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo sysUserQueryVo) {
        return baseMapper.selectPage(pageParam, sysUserQueryVo );
    }

    @Override
    public void updateStatus(String id, Integer status) {
        // 根据用户id查询
        SysUser sysUser = baseMapper.selectById(id);
        // 设置修改状态
        sysUser.setStatus(status);
        // 调用方法修改
        baseMapper.updateById(sysUser);
    }


    @Override
    public SysUser getUserInfoByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return baseMapper.selectOne(queryWrapper);
    }


    @Override
    public Map<String, Object> getUserInfo(String username) {
        // 根据username查询用户基本信息
        SysUser sysUser = this.getUserInfoByUsername(username);
        // 根据userid查询菜单权限值
        List<RouterVo> routerAuthList = sysMenuService.getUserMenuList(sysUser.getId());
        // 根据userid查询按钮权限值
        List<String> permsList = sysMenuService.getUserButtonList(sysUser.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("name",username);
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //当前权限控制使用不到，我们暂时忽略
        result.put("roles",  new HashSet<>());
        // 菜单权限数据
        result.put("routers", routerAuthList);
        // 按钮权限数据
        result.put("buttons", permsList);
        return result;
    }
}
