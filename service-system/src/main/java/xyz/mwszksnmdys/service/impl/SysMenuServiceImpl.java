package xyz.mwszksnmdys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mwszksnmdys.exception.MWSException;
import xyz.mwszksnmdys.mapper.SysMenuMapper;
import xyz.mwszksnmdys.mapper.SysRoleMenuMapper;
import xyz.mwszksnmdys.service.SysMenuService;
import xyz.mwszksnmdys.system.SysMenu;
import xyz.mwszksnmdys.system.SysRoleMenu;
import xyz.mwszksnmdys.utils.MenuHelper;
import xyz.mwszksnmdys.utils.RouterHelper;
import xyz.mwszksnmdys.vo.AssginMenuVo;
import xyz.mwszksnmdys.vo.RouterVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author mws
 * @since 2022-11-07
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    public List<SysMenu> findNodes() {
        // 获取所有菜单
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(null);
        // 所有菜单数据转换为要求的格式
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList;
    }

    @Override
    public void removeMenuById(String id) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw  new MWSException(201, "请先删除子菜单");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        // 获取所有菜单 status = 1
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<SysMenu> menuList = baseMapper.selectList(queryWrapper);
        // 根据角色id查询角色分配过的菜单列表
        LambdaQueryWrapper<SysRoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(roleMenuWrapper);
        // 从第二步查询的列表中获取角色分配的所有菜单id
        List<String> roleMenuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        // 数据处理 isSelected 被选中未true，否则为false
        menuList.forEach(item->item.setSelect(roleMenuIds.contains(item.getId())));
        // 转换成树形结构
        List<SysMenu> sysMenus = MenuHelper.buildTree(menuList);
        return sysMenus;
    }

    // 根据id获取菜单权限
    @Override
    public List<RouterVo> getUserMenuList(String id) {
        // admin是超级管理员，拥有操作所有内容的权限，id为1
        List<SysMenu> menuList = null;
        if("1".equals(id)){
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus, 1).
                    orderByDesc(SysMenu::getSortValue);
            menuList = baseMapper.selectList(queryWrapper);
        }else{
            // 非超级管理员，查询其权限
            menuList = baseMapper.findMenuListUserId(id);
        }
        // 构建树形结构
        List<SysMenu> treeList = MenuHelper.buildTree(menuList);
        // 转换成前端路由要求的格式
        List<RouterVo> routerVoList = RouterHelper.buildRouters(treeList);
        return routerVoList;
    }

    // 根据id获取按钮权限
    @Override
    public List<String> getUserButtonList(String userId) {
        List<SysMenu> sysMenuList = null;
        if("1".equals(userId)){
            sysMenuList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getStatus, 1).orderByDesc(SysMenu::getSortValue));
        }else {
            sysMenuList = baseMapper.findMenuListUserId(userId);
        }
        List<String> permsList = sysMenuList.stream().filter(item->item.getType() == 2).
                map(SysMenu::getPerms).collect(Collectors.toList());
        return permsList;
    }

    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        // 根据角色id删除菜单权限
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(queryWrapper);
        // 遍历菜单id列表，进行添加
        List<String> menuIdList = assginMenuVo.getMenuIdList();
        menuIdList.forEach(menuId ->{
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuMapper.insert(sysRoleMenu);
        });

    }
}
