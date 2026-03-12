<template>
    <div>
        <!-- 搜索表单卡片 -->
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <el-row>
                    <el-button type="primary" size="default" @click="SearchClick">
                        <el-icon>
                            <Search />
                        </el-icon>查询
                    </el-button>
                    <el-button type="warning" size="default" @click="ResetClick">
                        <el-icon>
                            <Refresh />
                        </el-icon>清空条件
                    </el-button>
                </el-row>
            </div>
            <div class="margin-top-sm">
                <el-form :inline="true" :model="searchForm" size="default">
                    <el-form-item label="提醒内容" prop="Content">
                        <el-input v-model.trim="searchForm.Content"  placeholder="请输入提醒内容"  :clearable="true" ></el-input>
                    </el-form-item>
                    <el-form-item label="提醒标题" prop="Title">
                        <el-input v-model.trim="searchForm.Title"  placeholder="请输入提醒标题"  :clearable="true" ></el-input>
                    </el-form-item>
                    <el-form-item label="提醒方式" prop="RemindType">
                        <el-input v-model.trim="searchForm.RemindType"  placeholder="请输入提醒方式"  :clearable="true" ></el-input>
                    </el-form-item>
                    <el-form-item label="提醒人">
                        <SigleSelect url="/User/List" class="search-input" columnName="Name" :clearable="true" columnValue="Id"
                            v-model="searchForm.PublishUserId">
                        </SigleSelect>
                    </el-form-item>                
                    <el-form-item label="提醒时间" class="search-input">
                        <el-date-picker v-model="searchForm.RemindTimeRange" type="datetimerange" start-placeholder="开始提醒时间"
                            end-placeholder="结束提醒时间" value-format="YYYY-MM-DD HH:mm:ss" >
                        </el-date-picker>                                       
                    </el-form-item>                
 							 <el-form-item label="是否提醒" class="search-input">
                 		 <el-select v-model="searchForm.IsRemind" class="search-input" :clearable="true" placeholder="请选择是否提醒" >
                          <el-option  key="true"  label="是"  value="true">
                          </el-option>
                           <el-option  key="false"  label="否"  value="false">
                          </el-option>
                        </el-select> 
                    </el-form-item>   
                </el-form>
            </div>
        </el-card>
<!-- 编辑对话框 -->
        <el-dialog :title="formData.Id ? '修改健康提醒' : '添加健康提醒'" v-model="editorShow" width="50%" :lock-scroll="true"
            min-height="500px">
            <el-form v-if="editorShow" ref="editModalForm" :rules="editModalFormRules" :model="formData" label-width="140px"
                size="default">             
              <el-row :gutter="10" class="edit-from-body">

                    <el-col :span="24">
                        <el-form-item label="提醒标题" prop="Title">
                            <el-input type="text" v-model="formData.Title"  placeholder="请输入提醒标题"     :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>


                    <el-col :span="24">
                        <el-form-item label="提醒人" prop="PublishUserId">
                          <SigleSelect url="/User/List" columnName="Name" columnValue="Id"  v-model="formData.PublishUserId" >
                          </SigleSelect>
                        </el-form-item>
                    </el-col>

                    <el-col :span="24">
                        <el-form-item label="提醒次数" prop="Num">
                            <el-input-number  v-model="formData.Num"  placeholder="请输入提醒次数"     :clearable="true"  :min="1" :max="1000000"></el-input-number>
                        </el-form-item>
                    </el-col>

                    <el-col :span="24">
                        <el-form-item label="提醒时间" prop="RemindTime">
                            <el-date-picker v-model="formData.RemindTime" align="right" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>

                    <el-col :span="24">
                        <el-form-item label="是否提醒" prop="IsRemind">
                       		  <el-switch v-model="formData.IsRemind"  :active-value="true" :inactive-value="false">
                            </el-switch>                          
                        </el-form-item>
                    </el-col>

                    <el-col :span="24">
                        <el-form-item label="提醒内容" prop="Content">
                            <el-input type="text" v-model="formData.Content"  placeholder="请输入提醒内容"     :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>


                    <el-col :span="24">
                        <el-form-item label="提醒方式" prop="RemindType">
                            <el-input type="text" v-model="formData.RemindType"  placeholder="请输入提醒方式"     :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>

                </el-row>

                <el-row type="flex" justify="end" align="bottom">
                    <el-form-item>
                        <el-button size="default" type="primary" plain @click="CreateOrEditForm()">确 定</el-button>
                        <el-button size="default" @click="editorShow = false">取 消</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
        </el-dialog>




        <!-- 数据表格 -->
        <PaginationTable ref="PaginationTableId" url="/HealthNotice/List" :column="columnList">
            <template v-slot:header>
                <el-button type="primary" size="default" @click="ShowEditModal()">
                    <el-icon>
                        <Edit />
                    </el-icon>新 增
                </el-button>
                <el-button type="danger" size="default" @click="BatchDelete">
                    <el-icon>
                        <Delete />
                    </el-icon>批量删除
                </el-button>
            </template>
            <template v-slot:Operate="scope">
              <el-button type="primary" size="default" class="margin-top-xs" @click="ShowEditModal(scope.row.Id)">
                    <el-icon>
                        <Edit />
                    </el-icon>修 改
                </el-button>
                <el-button type="danger" size="default" class="margin-top-xs" @click="ShowDeleteModal(scope.row.Id)">
                    <el-icon>
                        <Delete />
                    </el-icon>删 除
                </el-button>
            </template>
        </PaginationTable>
        
              
              
    </div>
</template>

<script setup>
import { Post,PostSingleUpdate } from '@/api/http';
import { ColumnType } from '@/components/Tables/columnTypes';
import { useCommonStore } from "@/store";

import { Delete, Edit, Refresh, Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {onMounted,onBeforeMount,nextTick, watch,computed, reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();
const commonStore = useCommonStore();
// 计算属性
const Token = computed(() => commonStore.Token)
const UserInfo = computed(() => commonStore.UserInfo)
const RoleType = computed(() => commonStore.RoleType)
const UserId = computed(() => commonStore.UserId)

// 搜索表单数据
const searchForm = reactive({});

// 编辑表单数据
const formData = reactive({});

// 编辑对话框显示状态
const editorShow = ref(false);

// 表单引用
const editModalForm = ref(null);

// 表格列配置
const columnList = ref([
  {
    key: "Id",
    hidden: true,

  },
  {
    key: "PublishUserId",
    hidden: true, 
  },
  {
    key: "PublishUserDto.Name",
    title: "名称",
    width: "160px",
        
    type: ColumnType.SHORTTEXT, 
  },
  {
    key: "Content",
    title: "提醒内容",
    width: "160px",
        
    type: ColumnType.SHORTTEXT, 
  },
  {
    key: "Title",
    title: "提醒标题",
    width: "160px",
        
    type: ColumnType.SHORTTEXT, 
  },
  {
    key: "Num",
    title: "提醒次数",
    width: "160px",
        
    type: ColumnType.SHORTTEXT, 
  },
  {
    key: "RemindTime",
    title: "提醒时间",
    width: "140px",
    type:ColumnType.DATE,
  },
  {
    key: "RemindType",
    title: "提醒方式",
    width: "160px",
        
    type: ColumnType.SHORTTEXT, 
  },
  {
    key: "IsRemind",
    title: "是否提醒",
    width: "140px",
    type: ColumnType.JUDGMENTTAG,
  },
  {
    title: "操作",
    width:"300px",
    key: "Operate",
    type: ColumnType.USERDEFINED,
  },
            ]);

// 表单验证规则
const editModalFormRules = reactive({
"Title":[
{ required: true, message: '该项为必填项', trigger: 'blur' },
              ],           
"PublishUserId":[
{ required: true, message: '该项为必填项', trigger: 'blur' },
              ],           
"Num":[
{ required: true, message: '该项为必填项', trigger: 'blur' },
              ],           
"RemindTime":[
{ required: true, message: '该项为必填项', trigger: 'blur' },
              ],           
"IsRemind":[
{ required: true, message: '该项为必填项', trigger: 'blur' },
              ],           
"Content":[
{ required: true, message: '该项为必填项', trigger: 'blur' },
              ],           
"RemindType":[
{ required: true, message: '该项为必填项', trigger: 'blur' },
              ],           
 });


// 表格引用
const PaginationTableId = ref(null);

// 显示编辑对话框
const ShowEditModal = async (Id) => {

    const { Data } = await Post(`/HealthNotice/Get`, { Id: Id });
    
    Object.assign(formData, Data);

    editorShow.value = true;

};

// 创建或编辑表单
const CreateOrEditForm = async () => {
    if (!editModalForm.value) return;

    await editModalForm.value.validate(async valid => {
        if (valid) {
            const { Success } = await Post(`/HealthNotice/CreateOrEdit`, formData);

            if (Success) {
                editorShow.value = false;
                PaginationTableId.value.Reload(searchForm);
                ElMessage.success('操作成功');
            }
        }
    });
};

// 搜索点击
const SearchClick = () => {
    PaginationTableId.value.Reload(searchForm);
};

// 重置搜索条件
const ResetClick = () => {
    Object.keys(searchForm).forEach(key => {
        searchForm[key] = undefined;
    });
    PaginationTableId.value.Reload(searchForm);
};

// 显示删除确认框
const ShowDeleteModal = async (Id) => {

    try {
        await ElMessageBox.confirm('确认删除该测试信息吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const { Success } = await Post(`/HealthNotice/Delete`, { Id: Id });
        if (Success) {
            PaginationTableId.value.Reload(searchForm);
            ElMessage.success('删除成功');
        }
    }
    catch (error) {
        ElMessage.warning('用户放弃了操作');
    }
};

// 批量删除
const BatchDelete = async () => {
    const ids = PaginationTableId.value.GetSelectionRow().map(x => x.Id);
    if (ids.length > 0) {
        try {
            await ElMessageBox.confirm('确认删除所选的行数据吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })


            const { Success } = await Post(`/HealthNotice/BatchDelete`, { Ids: ids });
            if (Success) {
                PaginationTableId.value.Reload(searchForm);
                ElMessage.success('删除成功');
            }
        }
        catch (error) {
            ElMessage.warning('用户放弃了操作');
        }
    }
    else {
        ElMessage.warning('用户放弃了操作');
    }
};
onBeforeMount(() => {
   
});
</script>

<style scoped>

</style>