package xyz.mwszksnmdys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import xyz.mwszksnmdys.system.SysLoginLog;

@Repository
@Mapper
public interface LoginLogMapper extends BaseMapper<SysLoginLog> {
}
