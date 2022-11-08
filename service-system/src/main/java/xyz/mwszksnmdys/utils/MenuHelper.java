package xyz.mwszksnmdys.utils;

import xyz.mwszksnmdys.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    // 构建树形结构
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        // 创建集合封装最终数据
        List<SysMenu> trees = new ArrayList<>();
        // 遍历所有菜单集合
        for (SysMenu sysMenu : sysMenuList) {
            // 找到递归入口
            if (sysMenu.getParentId() == 0) {
                trees.add(findChildren(sysMenu, sysMenuList));
            }
        }
        return trees;
    }

    private static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());

        for (SysMenu it : sysMenuList) {
            if (Long.parseLong(sysMenu.getId()) == it.getParentId()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(it, sysMenuList));
            }
        }
        return sysMenu;
    }
}
