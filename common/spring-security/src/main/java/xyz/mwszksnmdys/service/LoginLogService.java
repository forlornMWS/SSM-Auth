package xyz.mwszksnmdys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.mwszksnmdys.system.SysLoginLog;
import xyz.mwszksnmdys.vo.SysLoginLogQueryVo;

public interface LoginLogService {

    public void recordLoginLog(String username, Integer status,
                               String ipAddr, String message);

    IPage<SysLoginLog> selectPage(Long page, Long limit, SysLoginLogQueryVo loginLogQueryVo);
}
