package xyz.mwszksnmdys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.mwszksnmdys.mapper.LoginLogMapper;
import xyz.mwszksnmdys.service.LoginLogService;
import xyz.mwszksnmdys.system.SysLoginLog;
import xyz.mwszksnmdys.vo.SysLoginLogQueryVo;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void recordLoginLog(String username, Integer status, String ipAddr, String message) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setStatus(status);
        sysLoginLog.setIpaddr(ipAddr);
        sysLoginLog.setMsg(message);
        loginLogMapper.insert(sysLoginLog);
    }

    @Override
    public IPage<SysLoginLog> selectPage(Long page, Long limit, SysLoginLogQueryVo loginLogQueryVo) {
        Page<SysLoginLog> pageParam = new Page<>(page, limit);
        String username = loginLogQueryVo.getUsername();
        String createTimeBegin = loginLogQueryVo.getCreateTimeBegin();
        String createTimeEnd = loginLogQueryVo.getCreateTimeEnd();
        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(username)){
            queryWrapper.like(SysLoginLog::getUsername, username);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            queryWrapper.ge(SysLoginLog::getCreateTime, createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            queryWrapper.le(SysLoginLog::getCreateTime, createTimeEnd);
        }
        return loginLogMapper.selectPage(pageParam, queryWrapper);
    }
}
