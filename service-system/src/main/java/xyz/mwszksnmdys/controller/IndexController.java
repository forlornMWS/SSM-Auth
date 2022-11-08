package xyz.mwszksnmdys.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.mwszksnmdys.exception.MWSException;
import xyz.mwszksnmdys.result.Result;
import xyz.mwszksnmdys.service.SysUserService;
import xyz.mwszksnmdys.system.SysUser;
import xyz.mwszksnmdys.utils.JwtHelper;
import xyz.mwszksnmdys.utils.MD5;
import xyz.mwszksnmdys.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    // login
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        // 根据username查询数据
        SysUser sysUser = sysUserService.getUserInfoByUsername(loginVo.getUsername());
        // 如果查询为空
        if(sysUser == null){
            throw  new MWSException(20001, "用户不存在");
        }
        // 判断密码是否一致
        String password = loginVo.getPassword();
        if(!MD5.encrypt(password).equals(sysUser.getPassword())){
            throw new MWSException(20001, "密码错误");
        }
        // 判断用户是否可用
        if(sysUser.getStatus() == 0){
            throw new MWSException(20001, "用户被禁用");
        }
        // 根据userid和username生成token，通过map返回
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);
    }

    // info
    @GetMapping("/info")
    public Result info(HttpServletRequest httpServletRequest){
        // 获取请求头的token字符串
        String token = httpServletRequest.getHeader("token");
        // 从token字符串获取用户名称（id）
        String username = JwtHelper.getUsername(token);
        // 根据用户名称获取用户信息
        Map<String, Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }
}
