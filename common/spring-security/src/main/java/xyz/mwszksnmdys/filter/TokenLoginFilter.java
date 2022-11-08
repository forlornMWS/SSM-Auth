package xyz.mwszksnmdys.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import xyz.mwszksnmdys.custom.CustomUser;
import xyz.mwszksnmdys.result.Result;
import xyz.mwszksnmdys.result.ResultCodeEnum;
import xyz.mwszksnmdys.service.LoginLogService;
import xyz.mwszksnmdys.utils.IpUtil;
import xyz.mwszksnmdys.utils.JwtHelper;
import xyz.mwszksnmdys.utils.ResponseUtil;
import xyz.mwszksnmdys.vo.LoginVo;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;

    private LoginLogService loginLogService;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate,
                            LoginLogService loginLogService) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
        this.redisTemplate = redisTemplate;
        this.loginLogService = loginLogService;
    }

    // 登录认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            Authentication authRequest = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 登录成功
    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        // 获取认证对象
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        redisTemplate.opsForValue().set(customUser.getUsername(), JSON.toJSONString(customUser.getAuthorities()));
        // 生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());
        // 记录登录日志
        loginLogService.recordLoginLog(customUser.getUsername(), 1, IpUtil.getIpAddress(request), "登录成功");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    // 登录失败
    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request,
                                           HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {

        if(failed.getCause() instanceof RuntimeException) {
            ResponseUtil.out(response, Result.build(null, 204, failed.getMessage()));
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
        }
    }
}
