package xyz.mwszksnmdys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mwszksnmdys.mapper.SysRoleMapper;
import xyz.mwszksnmdys.mapper.SysUserRoleMapper;
import xyz.mwszksnmdys.service.SysRoleService;
import xyz.mwszksnmdys.system.SysRole;
import xyz.mwszksnmdys.system.SysUserRole;
import xyz.mwszksnmdys.vo.AssginRoleVo;
import xyz.mwszksnmdys.vo.SysRoleQueryVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public Map<String, Object> getRoleByUserId(String userId) {
        // 获取所有角色
        List<SysRole> roles = baseMapper.selectList(null);
        // 根据用户id进行查询已经分配的角色
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(queryWrapper);
        // 获取用户已分配角色的id
        List<String> userRoleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("allRoles", roles);
        returnMap.put("userRoleIds", userRoleIds);
        return returnMap;
    }

    // 用户分配角色
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        // 根据用户id删除之前已经分配的角色
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", assginRoleVo.getUserId());
        sysUserRoleMapper.delete(queryWrapper);
        // 获取所有的角色id，添加到角色用户关系表
        // 角色id列表
        List<String> roleIdList = assginRoleVo.getRoleIdList();
        roleIdList.forEach(roleId->{
            if(roleId != null) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assginRoleVo.getUserId());
                sysUserRole.setRoleId(roleId);
                sysUserRoleMapper.insert(sysUserRole);
            }
        });
    }


    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo) {
        return sysRoleMapper.selectPage(pageParam, sysRoleQueryVo);
    }
}
