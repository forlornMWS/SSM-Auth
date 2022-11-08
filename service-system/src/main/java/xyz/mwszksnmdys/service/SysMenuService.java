package xyz.mwszksnmdys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.mwszksnmdys.system.SysMenu;
import xyz.mwszksnmdys.vo.AssginMenuVo;
import xyz.mwszksnmdys.vo.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author mws
 * @since 2022-11-07
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(String id);

    List<SysMenu> findSysMenuByRoleId(Long roleId);

    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> getUserMenuList(String id);

    List<String> getUserButtonList(String id);
}
