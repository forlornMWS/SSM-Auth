package xyz.mwszksnmdys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.mwszksnmdys.system.SysOperLog;
import xyz.mwszksnmdys.vo.SysOperLogQueryVo;

public interface OperaLogService {
    public void saveSysLog(SysOperLog sysOperLog);

    IPage<SysOperLog> selectPage(Page<SysOperLog> pageParam, SysOperLogQueryVo sysOperLogQueryVo);
}
