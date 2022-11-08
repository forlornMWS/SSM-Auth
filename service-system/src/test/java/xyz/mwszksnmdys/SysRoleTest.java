package xyz.mwszksnmdys;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.mwszksnmdys.mapper.SysRoleMapper;
import xyz.mwszksnmdys.system.SysRole;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class SysRoleTest{

    @Resource
    private SysRoleMapper sysRoleMapper;

    @DisplayName("查询表中所有数据")
    @Test
    public void testAdd(){
        List<SysRole> list = sysRoleMapper.selectList(null);
        list.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");

        int result = sysRoleMapper.insert(sysRole);
        System.out.println(result); //影响的行数
        System.out.println(sysRole.getId()); //id自动回填
    }

    @Test
    public void testUpdateById(){
        SysRole sysRole = new SysRole();
        sysRole.setId("1");
        sysRole.setRoleName("角色管理员1");

        int result = sysRoleMapper.updateById(sysRole);
        System.out.println(result);

    }
}