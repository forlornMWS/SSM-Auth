package xyz.mwszksnmdys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.mwszksnmdys.mapper.OperaLogMapper;
import xyz.mwszksnmdys.service.OperaLogService;
import xyz.mwszksnmdys.system.SysLoginLog;
import xyz.mwszksnmdys.system.SysOperLog;
import xyz.mwszksnmdys.vo.SysOperLogQueryVo;

@Service
public class OperaLogServiceImpl implements OperaLogService {
    @Autowired
    private OperaLogMapper operaLogMapper;

    @Override
    public void saveSysLog(SysOperLog sysOperLog) {
        operaLogMapper.insert(sysOperLog);
    }

    @Override
    public IPage<SysOperLog> selectPage(Page<SysOperLog> pageParam, SysOperLogQueryVo sysOperLogQueryVo) {
        String operName = sysOperLogQueryVo.getOperName();
        String title = sysOperLogQueryVo.getTitle();
        String createTimeBegin = sysOperLogQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysOperLogQueryVo.getCreateTimeEnd();
        LambdaQueryWrapper<SysOperLog> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(operName)){
            queryWrapper.like(SysOperLog::getOperName, operName);
        }
        if(!StringUtils.isEmpty(title)){
            queryWrapper.like(SysOperLog::getTitle, operName);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            queryWrapper.ge(SysOperLog::getCreateTime, createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            queryWrapper.le(SysOperLog::getCreateTime, createTimeEnd);
        }
        return operaLogMapper.selectPage(pageParam, queryWrapper);
    }


}
