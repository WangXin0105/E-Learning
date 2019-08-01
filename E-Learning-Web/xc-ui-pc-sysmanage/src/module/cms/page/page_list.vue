<template>
  <div>
    <el-form :model="params">
      <el-select v-model="params.siteId" placeholder="请选择站点">
        <el-option
          v-for="item in siteList"
          :key="item.siteId"
          :label="item.pageName"
          :value="item.siteId">
        </el-option>
      </el-select>
      页面别名：
      <el-input v-model="params.pageAliase" style="width: 100px"></el-input>
      <el-button type="primary" size="medium" v-on:click="query">查询</el-button>
      <router-link :to="{path:'/cms/page/add',query:{
        page:this.params.page,
        siteId:this.params.siteId
      }}">
        <el-button type="primary" size="medium">新增页面</el-button>
      </router-link>
    </el-form>
    <el-table
      :data="list"
      stripe
      style="width: 100%">
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="pageName" label="页面名称" width="120">
      </el-table-column>
      <el-table-column prop="pageAliase" label="别名" width="120">
      </el-table-column>
      <el-table-column prop="pageType" label="页面类型" width="150">
      </el-table-column>
      <el-table-column prop="pageWebPath" label="访问路径" width="250">
      </el-table-column>
      <el-table-column prop="pagePhysicalPath" label="物理路径" width="250">
      </el-table-column>
      <el-table-column prop="pageCreateTime" label="创建时间" width="180">
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template slot-scope="page">
          <el-button
            size="small" type="text"
            @click="edit(page.row.pageId)">编辑
          </el-button>
          <el-button
            size="small" type="text"
            @click="del(page.row.pageId)">删除
          </el-button>
          <el-button
            size="small" type="text"
            @click="preview(page.row.pageId)">页面预览
          </el-button>
          <el-button
            size="small" type="text"
            @click="postPage(page.row.pageId)">发布
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      layout="prev, pager, next"
      :total="total"
      :page-size="params.size"
      :current-page="params.page"
      v-on:current-change="changePage"
      style="float:right">
    </el-pagination>
  </div>
</template>
<script>
  import * as cmsApi from '../api/cms'

  export default {
    data() {
      return {
        siteList: [],
        list: [],
        total: 0,
        params: {
          siteId: '',
          pageAliase: '',
          page: 1,
          size: 10
        }
      }
    },
    methods: {
      query: function () {
        cmsApi.page_list(this.params.page, this.params.size, this.params).then((res) => {
          this.list = res.queryResult.list;
          this.total = res.queryResult.total;
        })
      },
      changePage: function (page) {
        this.params.page = page;
        this.query()
      },
      getSiteSelect: function () {
        cmsApi.getSiteList().then((res) => {
          this.siteList = res;
        })
      },
      edit: function (pageId) {
        this.$router.push({
          path: '/cms/page/edit/' + this.params.page + '/' + pageId
        })
      },
      del: function (pageId) {
        this.$confirm('您确认删除吗?', '提示', {}).then(() => {
          cmsApi.page_del(pageId).then(res => {
            if (res.success) {
              this.$message.success("删除成功")
              this.query()
            } else {
              this.$message.error("删除失败")
            }
          })
        })
      },
      preview: function (pageId) {
        window.open("http://www.xuecheng.com:8888/cms/preview/" + pageId);
      },
      postPage: function (id) {
        this.$confirm('确认发布该页面吗?', '提示', {}).then(() => {
          cmsApi.page_postPage(id).then((res) => {
            if (res.success) {
              this.$message.success('发布成功，请稍后查看结果');
            } else {
              this.$message.error('发布失败');
            }
          });
        });
      }
    },
    created() {
      this.params.page = Number.parseInt(this.$route.query.page || 1)
      this.params.siteId = this.$route.query.siteId || ''
    },
    mounted() {
      this.getSiteSelect();
      this.query();
    }
  }
</script>
<style>
</style>
