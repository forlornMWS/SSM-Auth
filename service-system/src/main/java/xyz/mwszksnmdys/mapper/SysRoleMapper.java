package xyz.mwszksnmdys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.mwszksnmdys.system.SysRole;
import xyz.mwszksnmdys.vo.SysRoleQueryVo;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole>  {

    IPage<SysRole> selectPage(Page<SysRole> page, @Param("vo") SysRoleQueryVo roleQueryVo);
}
