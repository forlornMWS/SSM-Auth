package xyz.mwszksnmdys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import xyz.mwszksnmdys.custom.CustomUser;
import xyz.mwszksnmdys.service.SysMenuService;
import xyz.mwszksnmdys.service.SysUserService;
import xyz.mwszksnmdys.system.SysUser;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private SysMenuService sysMenuService;

    private static SimpleGrantedAuthority apply(String str) {
        return new SimpleGrantedAuthority(str.trim());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserInfoByUsername(username);
        if(sysUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        if(sysUser.getStatus() == 0){
            throw new RuntimeException("用户被禁用");
        }
        List<String> userPermsList = sysMenuService.getUserButtonList(sysUser.getId());
        // 转换为Security要求的格式数据
        List<SimpleGrantedAuthority> authorities = userPermsList.stream().map(UserDetailsServiceImpl::apply).collect(Collectors.toList());
        return new CustomUser(sysUser,authorities);
    }
}
