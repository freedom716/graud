<template>
    <div>
        <!-- 面包屑导航 -->
        <div class="breadcrumb">
            <span class="breadcrumb-label">您当前的位置:</span>
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>

            </el-breadcrumb>
        </div>
        <!-- 页面标题 -->
        <h1 class="page-title">Vue3 基础框架首页</h1>

        <!-- 内容卡片 -->
        <div class="content-card">
            <el-card class="card">
                <template #header>
                    <div class="card-header">
                        <span>欢迎使用</span>
                    </div>
                </template>
                <div class="card-content">
                    <p>这是一个基于Vue3的前端框架，集成了以下技术：</p>
                    <ul class="tech-list">
                        <li v-for="(tech, index) in techList" :key="index" class="tech-item">
                            <el-tag :type="tagTypes[index % tagTypes.length]">{{ tech }}</el-tag>
                        </li>
                    </ul>

                </div>
            </el-card>
        </div>
    </div>
</template>

<script >
export default {
    name: 'HomePage',
    data() {
        return {
            techList: [
                'Vue 3',
                'Pinia 状态管理',
                'Vue Router',
                'Axios',
                'Element Plus'

            ],
            tagTypes: ['', 'success', 'info', 'warning', 'danger']
        }
    },
    methods: {

    }
}
</script>

<style scoped lang="scss">
/* 首页容器 */
.home-container {
    padding: 20px;
}

/* 页面标题 */
.page-title {
    font-size: 1.5rem;
    font-weight: bold;
    text-align: center;
    margin: 1.5rem 0;
}

/* 卡片 */
.card {
    width: 100%;
    max-width: 48rem;
    margin: 0 auto;
}

/* 技术列表 */
.tech-list {
    margin: 20px 0;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

/* 技术项 */
.tech-item {
    list-style: none;
}

/* 操作按钮 */
.action-buttons {
    margin-top: 20px;
    display: flex;
    justify-content: center;
}
</style> 