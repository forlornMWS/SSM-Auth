package xyz.mwszksnmdys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.mwszksnmdys.result.Result;
import xyz.mwszksnmdys.service.LoginLogService;
import xyz.mwszksnmdys.system.SysLoginLog;
import xyz.mwszksnmdys.vo.SysLoginLogQueryVo;

@Api(value = "SysLoginLog管理", tags = "SysLoginLog管理")
@RestController
@RequestMapping(value = "/admin/system/sysLoginLog")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysLoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    // 条件分页查询登录日志
    @ApiOperation("分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页显示数量", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "loginLogQueryVo", value = "查询对象", required = false)
            SysLoginLogQueryVo loginLogQueryVo) {
        IPage<SysLoginLog> pageModel = loginLogService.selectPage(page, limit, loginLogQueryVo);
        return Result.ok(pageModel);
    }
}
