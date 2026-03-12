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
                    <el-form-item label="单位名称" prop="UnitName">
                        <el-input v-model.trim="searchForm.UnitName" placeholder="请输入单位名称" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="食物">
                        <SigleSelect url="/Food/List" class="search-input" columnName="Name" :clearable="true"
                            columnValue="Id" v-model="searchForm.FoodId">
                        </SigleSelect>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
        <!-- 编辑对话框 -->
        <el-dialog :title="formData.Id ? '修改食物单位' : '添加食物单位'" v-model="editorShow" width="50%" :lock-scroll="true"
            min-height="500px">
            <el-form v-if="editorShow" ref="editModalForm" :rules="editModalFormRules" :model="formData" label-width="140px"
                size="default">
                <el-row :gutter="10" class="edit-from-body">

                    <el-col :span="24">
                        <el-form-item label="食物" prop="FoodId">
                            <SigleSelect url="/Food/List" columnName="Name" columnValue="Id" v-model="formData.FoodId">
                            </SigleSelect>
                        </el-form-item>
                    </el-col>


                    <el-col :span="24">
                        <el-form-item label="单位名称" prop="UnitName">
                            <el-input type="text" v-model="formData.UnitName" placeholder="请输入单位名称"
                                :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="单位值" prop="UnitValue">
                            <el-input-number v-model="formData.UnitValue" placeholder="请输入单位值（对应的克数）" :clearable="true"
                                :min="1" :max="1000000"></el-input-number>
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
        <PaginationTable ref="PaginationTableId" url="/FoodUnit/List" :column="columnList">
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
import { Post, PostSingleUpdate } from '@/api/http';
import { ColumnType } from '@/components/Tables/columnTypes';
import { useCommonStore } from "@/store";

import { Delete, Edit, Refresh, Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { onMounted, onBeforeMount, nextTick, watch, computed, reactive, ref } from 'vue';
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
        key: "FoodId",
        hidden: true,
    },
    {
        key: "FoodDto",
        title: "食物",
        hidden: true,
    },
    {
        key: "FoodDto.Name",
        title: "食物名称",

        type: ColumnType.SHORTTEXT,
    }, {
        key: "FoodDto.Cover",
        title: "食物名称",

        type: ColumnType.IMAGES,
    },
    {
        key: "UnitName",
        title: "单位名称",

        type: ColumnType.SHORTTEXT,
    },
    {
        key: "UnitValue",
        title: "单位值(对应克)",

        type: ColumnType.SHORTTEXT,

    },
    {
        key: "Calories",
        title: "热量",
        width: "160px",

        type: ColumnType.SHORTTEXT,
        template: function (row) {
            return window.ToFixed3(row.FoodDto.Calories * row.UnitValue);
        }
    },
    {
        key: "Protein",
        title: "蛋白质",
        width: "160px",

        type: ColumnType.SHORTTEXT,
        template: function (row) {
            return window.ToFixed3(row.FoodDto.Protein * row.UnitValue);
        }
    },
    {
        key: "Carbohydrates",
        title: "糖水化合物",
        width: "160px",

        type: ColumnType.SHORTTEXT,
        template: function (row) {
            return window.ToFixed3(row.FoodDto.Carbohydrates * row.UnitValue);
        }
    },
    {
        key: "Fat",
        title: "脂肪",
        width: "160px",

        type: ColumnType.SHORTTEXT,
        template: function (row) {
            return window.ToFixed3(row.FoodDto.Fat * row.UnitValue);
        }
    },
    {
        title: "操作",
        width: "300px",
        key: "Operate",
        type: ColumnType.USERDEFINED,
    },
]);

// 表单验证规则
const editModalFormRules = reactive({
    "FoodId": [
        { required: true, message: '该项为必填项', trigger: 'blur' },
    ],
    "UnitValue": [
        { required: true, message: '该项为必填项', trigger: 'blur' },
    ],
    "UnitName": [
        { required: true, message: '该项为必填项', trigger: 'blur' },
    ],
});


// 表格引用
const PaginationTableId = ref(null);

// 显示编辑对话框
const ShowEditModal = async (Id) => {

    const { Data } = await Post(`/FoodUnit/Get`, { Id: Id });

    Object.assign(formData, Data);

    editorShow.value = true;

};

// 创建或编辑表单
const CreateOrEditForm = async () => {
    if (!editModalForm.value) return;

    await editModalForm.value.validate(async valid => {
        if (valid) {
            const { Success } = await Post(`/FoodUnit/CreateOrEdit`, formData);

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
        const { Success } = await Post(`/FoodUnit/Delete`, { Id: Id });
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


            const { Success } = await Post(`/FoodUnit/BatchDelete`, { Ids: ids });
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

<style scoped></style>