package xyz.mwszksnmdys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xyz.mwszksnmdys.system.SysUser;
import xyz.mwszksnmdys.vo.SysUserQueryVo;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author mws
 * @since 2022-11-06
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    IPage<SysUser> selectPage(Page<SysUser> pageParam, @Param("vo") SysUserQueryVo sysUserQueryVo);
}
