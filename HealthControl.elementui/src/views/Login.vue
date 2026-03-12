<template>
    <div class="login-container">
        <!-- 背景装饰 -->
        <div class="bg-decoration">
            <div class="circle circle-1"></div>
            <div class="circle circle-2"></div>
            <div class="circle circle-3"></div>
        </div>

        <!-- 登录卡片 -->
        <div class="login-card">
            <!-- 头部logo和标题 -->
            <div class="login-header">
                <div class="logo-container">
                    <div class="health-icon">
                        <img src="@/assets/logo.png" alt="健康管理系统" class="logo-image">
                    </div>
                    <div class="title-text">
                        <h1 class="main-title">健康管理</h1>
                        <p class="sub-title">个人健康小程序管理端</p>
                    </div>
                </div>
            </div>

            <!-- 登录表单 -->
            <div class="login-form-wrapper">
                <el-form ref="loginForm" :model="formData" :rules="rules" class="login-form" label-position="top">
                    <el-form-item label="账号" prop="UserName">
                        <el-input v-model="formData.UserName" placeholder="请输入账号" prefix-icon="User" size="large" />
                    </el-form-item>

                    <el-form-item label="密码" prop="Password">
                        <el-input v-model="formData.Password" type="password" show-password placeholder="请输入密码"
                            prefix-icon="Lock" size="large" />
                    </el-form-item>

                    <el-form-item label="验证码" prop="Code">
                        <div class="code-container">
                            <el-input v-model="formData.Code" placeholder="请输入验证码" size="large" />
                            <div class="code-image">
                                <ImageCode ref="validCodeRef" />
                            </div>
                        </div>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" class="login-button" size="large" @click="loginBtn">
                            <el-icon class="login-icon">
                                <Right />
                            </el-icon>
                            立即登录
                        </el-button>
                    </el-form-item>
                </el-form>

                <!-- 底部链接 -->
                <div class="login-footer">
                    <router-link :to="{ path: '/ForgetPassword' }" class="forget-link">
                        忘记密码？
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { Post } from '@/api/http'
import ImageCode from '@/components/Identifyingcode/ImageCode.vue'
import { useCommonStore } from '@/store'
import { ElMessage } from 'element-plus'
import { User, Lock, Right } from '@element-plus/icons-vue'
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

// 路由和状态管理
const router = useRouter()
const commonStore = useCommonStore()
// 表单引用
const loginForm = ref(null)
const validCodeRef = ref(null)

// 表单数据
const formData = reactive({
    UserName: '',
    Password: '',
    RoleType: '1',
    Code: ''
})

// 角色选项
const roleOptions = ref([])

// 表单校验规则
const rules = reactive({
    UserName: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 20, message: '账号长度应在3到20个字符之间', trigger: 'blur' }
    ],
    Password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
    ],
    RoleType: [
        { required: true, message: '请选择角色', trigger: 'blur' },
    ],
    Code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const identifyCode = validCodeRef.value.getCode()
                if (value !== identifyCode) {
                    callback(new Error('请输入正确的验证码'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ]
})

// 组件挂载时获取角色类型
onMounted(() => {
    getRoleTypeApi()
})

// 获取角色类型
const getRoleTypeApi = async () => {
    try {
        const { Data: { Items } } = await Post('/Select/RoleType')
        roleOptions.value = Items
    } catch (error) {
        console.error('获取角色类型失败', error)
    }
}

// 登录按钮点击事件
const loginBtn = () => {
    loginForm.value.validate(async (valid) => {
        if (valid) {
            try {
                const { Success } = await commonStore.Login(formData)

                if (Success) {
                    ElMessage.success('登录成功')
                    router.push({
                        path: '/Admin'
                    })
                }
            } catch (error) {
                console.error('登录失败', error)
            }
        } else {
            ElMessage.error('登录验证不通过')
            return false
        }
    })
}


</script>

<style scoped lang="scss">
/* 登录页面容器 */
.login-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #e8f5e8 0%, #f0f9ff 50%, #e0f2fe 100%);
    position: relative;
    overflow: hidden;
}

/* 背景装饰圆圈 */
.bg-decoration {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 0;
}

.circle {
    position: absolute;
    border-radius: 50%;
    background: rgba(74, 222, 128, 0.1);
    animation: float 6s ease-in-out infinite;
}

.circle-1 {
    width: 200px;
    height: 200px;
    top: 10%;
    left: 10%;
    animation-delay: 0s;
}

.circle-2 {
    width: 150px;
    height: 150px;
    top: 60%;
    right: 15%;
    background: rgba(59, 130, 246, 0.08);
    animation-delay: 2s;
}

.circle-3 {
    width: 100px;
    height: 100px;
    bottom: 20%;
    left: 20%;
    background: rgba(16, 185, 129, 0.12);
    animation-delay: 4s;
}

/* 浮动动画 */
@keyframes float {

    0%,
    100% {
        transform: translateY(0px);
    }

    50% {
        transform: translateY(-20px);
    }
}

/* 登录卡片 */
.login-card {
    width: 420px;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 24px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    overflow: hidden;
    position: relative;
    z-index: 1;
}

/* 登录头部 */
.login-header {
    padding: 40px 40px 20px;
    text-align: center;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    color: white;
    position: relative;
}

.login-header::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 20"><defs><pattern id="a" patternUnits="userSpaceOnUse" width="20" height="20"><circle cx="10" cy="10" r="1" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="20" fill="url(%23a)"/></svg>');
}

.logo-container {
    position: relative;
    z-index: 1;
}

.health-icon {
    width: 64px;
    height: 64px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 16px;
    backdrop-filter: blur(10px);
    overflow: hidden;
}

.logo-image {
    width: 40px;
    height: 40px;
    object-fit: contain;
}

.main-title {
    font-size: 28px;
    font-weight: 700;
    margin: 0 0 8px 0;
    letter-spacing: 1px;
}

.sub-title {
    font-size: 14px;
    margin: 0;
    opacity: 0.9;
    font-weight: 400;
}

/* 表单包装器 */
.login-form-wrapper {
    padding: 40px;
}

/* 登录表单 */
.login-form {
    .el-form-item {
        margin-bottom: 24px;
    }

    .el-form-item__label {
        color: #374151;
        font-weight: 600;
        font-size: 14px;
        line-height: 20px;
        margin-bottom: 8px;
    }

    :deep(.el-input__wrapper) {
        border-radius: 12px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        border: 1px solid #e5e7eb;
        transition: all 0.3s ease;

        &:hover {
            border-color: #10b981;
        }

        &.is-focus {
            border-color: #10b981;
            box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
        }
    }
}

/* 验证码容器 */
.code-container {
    display: flex;
    align-items: center;
    gap: 12px;
    width: 100%;
}

.code-image {
    flex-shrink: 0;
    border-radius: 8px;
    overflow: hidden;
}

/* 登录按钮 */
.login-button {
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 12px;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    border: none;
    margin-top: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    transition: all 0.3s ease;

    &:hover {
        background: linear-gradient(135deg, #059669 0%, #047857 100%);
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(16, 185, 129, 0.3);
    }

    &:active {
        transform: translateY(0);
    }
}

.login-icon {
    transition: transform 0.3s ease;
}

.login-button:hover .login-icon {
    transform: translateX(2px);
}

/* 底部链接 */
.login-footer {
    text-align: center;
    margin-top: 24px;
}

.forget-link {
    color: #10b981;
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.3s ease;

    &:hover {
        color: #059669;
        text-decoration: underline;
    }
}

/* 响应式设计 */
@media (max-width: 480px) {
    .login-card {
        width: 90%;
        margin: 20px;
        border-radius: 16px;
    }

    .login-header {
        padding: 30px 30px 20px;
    }

    .login-form-wrapper {
        padding: 30px;
    }

    .main-title {
        font-size: 24px;
    }

    .health-icon {
        width: 56px;
        height: 56px;
    }
}

@media (max-height: 700px) {
    .login-container {
        padding: 20px 0;
    }

    .login-header {
        padding: 30px 40px 15px;
    }

    .login-form-wrapper {
        padding: 30px 40px;
    }
}
</style>
