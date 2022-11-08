import request from '@/utils/request'

const api_name = '/admin/system/sysRole'

export default {
    // 列表
    getPageList(page, limit, searchObj) {
        return request({
            url: `${api_name}/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },

    // 根据id删除
    removeById(id) {
        return request({
            url: `${api_name}/remove/${id}`,
            method: 'delete'
        })
    },

    // 添加角色
    saveRole(role) {
        return request({
            url: `${api_name}/save`,
            method: 'post',
            // 传递json格式数据 
            data: role
        })
    },

    // 根据id查询
    getRoleId(id) {
        return request({
            url: `${api_name}/findRoleById/${id}`,
            method: 'post'
        })
    },

    // 最终修改
    update(role) {
        return request({
            url: `${api_name}/update`,
            method: 'post',
            // 传递json格式数据 
            data: role
        })
    },


    // 批量删除
    batchRemove(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: 'delete',
            data: idList

        })
    },
    //根据用户id查询用户已分配的角色
    getRolesByUserId(userId) {
        return request({
            url: `${api_name}/toAssign/${userId}`,
            method: 'get'
        })
    },

    //分配角色
    assignRoles(assginRoleVo) {
        return request({
            url: `${api_name}/doAssign`,
            method: 'post',
            data: assginRoleVo
        })
    }
}
