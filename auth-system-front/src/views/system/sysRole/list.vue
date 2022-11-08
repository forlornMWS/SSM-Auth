<template>
  <div class="app-container">
    角色列表
    <!--查询表单-->
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名称">
              <el-input style="width: 100%" v-model="searchObj.roleName" placeholder="角色名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="fetchData()">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        </el-row>
      </el-form>
    </div>

    <!-- 工具条 -->
    <div class="tools-div">
      <el-button type="success" :disabled="$hasBP('bnt.sysRole.add')  === false" icon="el-icon-plus" size="mini" @click="add">添 加</el-button>
      <el-button class="btn-add" size="mini" @click="batchRemove()">批量删除</el-button>
    </div>

    <!-- 表格 -->
    <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%;margin-top: 10px;"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" />
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row.id)" title="修改" />
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="删除" />
          <el-button type="warning" icon="el-icon-baseball" size="mini" @click="showAssignAuth(scope.row)"
            title="分配权限" />
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination :current-page="page" :total="total" :page-size="limit" style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper" @current-change="fetchData" />

    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
      <el-form ref="dataForm" :model="sysRole" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="角色名称">
          <el-input v-model="sysRole.roleName" />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="sysRole.roleCode" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
// 引入定义的接口
import api from '@/api/system/role'
export default {
  // 定义初始值
  data() {
    return {
      listLoading: true, // 显示加载
      list: [], // 角色列表
      total: 0, // 总记录数
      page: 1, // 当前页 
      limit: 3, // 每页显示的记录数
      searchObj: {}, // 条件查询封装对象
      sysRole: {},  // 封装添加表单的数据
      dialogVisible: false,
      selectValue: []  // 复选框选择的内容封装数组
    }
  },

  // 页面渲染之前执行
  created() {
    // 调用列表方法
    this.fetchData()
  },

  methods: {  // 具体的方法
    // 根据id删除角色
    removeDataById(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        return api.removeById(id)
      }).then((response) => {
        this.fetchData(this.page)
        this.$message.success(response.message || '删除成功')
      })
    },
    // 重置
    resetData() {
      // 清空表单
      this.searchObj = {},
        this.fetchData()
    },
    // 条件分页查询列表
    fetchData(pageNum = 1) {
      this.page = pageNum
      api.getPageList(this.page, this.limit, this.searchObj)
        .then(response => {
          this.listLoading = false
          console.log(response)
          // 每页数据列表
          this.list = response.data.records
          // 总记录数
          this.total = response.data.total
        })
    },

    // 点击添加按钮，弹框
    add() {
      this.dialogVisible = true
      this.sysRole = {}
    },

    // 点击确定
    saveOrUpdate() {
      // 判断添加还是修改
      if (!this.sysRole.id) {  // sysRoel中没有id
        this.saveRole()
      } else {
        this.updateRole()
      }
    },

    // 添加的方法
    saveRole() {
      api.saveRole(this.sysRole).then(reponse => {
        // 提示
        this.$message({
          type: "success",
          message: "添加成功"
        })
        // 关闭弹窗
        this.dialogVisible = false
        // 刷新数据
        this.fetchData()
      })
    },

    // 点击修改按钮-数据回显
    edit(id) {
      this.dialogVisible = true
      api.getRoleId(id).then(response => {
        this.sysRole = response.data
      })
    },

    // 修改的方法
    updateRole() {
      api.update(this.sysRole).then(response => {
        // 提示
        this.$message({
          type: "success",
          message: "修改成功"
        })
        // 关闭弹窗
        this.dialogVisible = false
        // 刷新数据
        this.fetchData()
      })
    },

    // 批量删除
    batchRemove() {
      if (this.selectValue.length == 0) {
        this.$message.warning('请选择需要删除的记录！')
        return
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        var idlist = []
        // 获取多个复选框对应的id，封装到数组中
        this.selectValue.forEach(element => {
          idlist.push(element.id)
          console.log(idlist)
        });
        return api.batchRemove(idlist)
      }).then((response) => {
        this.fetchData(this.page)
        this.$message.success(response.message || '删除成功')
      })
    },

    // 复选框变化执行方法
    handleSelectionChange(selection) {
      this.selectValue = selection
      console.log(this.selectValue)
    },

    // 跳转分配菜单权限
    showAssignAuth(row) {
      this.$router.push('/system/assignAuth?id=' + row.id + '&roleName=' + row.roleName);
    }

  }
}
</script>
