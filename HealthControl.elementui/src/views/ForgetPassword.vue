<template>
    <div class="forget-container">
        <!-- 背景装饰 -->
        <div class="bg-decoration">
            <div class="circle circle-1"></div>
            <div class="circle circle-2"></div>
            <div class="circle circle-3"></div>
        </div>

        <!-- 忘记密码卡片 -->
        <div class="forget-card">
            <!-- 头部logo和标题 -->
            <div class="forget-header">
                <div class="logo-container">
                    <div class="health-icon">
                        <img src="@/assets/logo.png" alt="健康饮食系统" class="logo-image">
                    </div>
                    <div class="title-text">
                        <h1 class="main-title">找回密码</h1>
                        <p class="sub-title">请填写相关信息以重置密码</p>
                    </div>
                </div>
            </div>

            <!-- 忘记密码表单 -->
            <div class="forget-form-wrapper">
                <el-form v-if="true" ref="loginFormRef" :model="formData" :rules="rules" class="forget-form"
                    label-position="top">
                    <el-form-item label="账号" prop="UserName">
                        <el-input v-model="formData.UserName" placeholder="请输入账号" prefix-icon="User" size="large" />
                    </el-form-item>

                    <el-form-item label="邮箱" prop="Email">
                        <el-input v-model="formData.Email" placeholder="请输入邮箱" prefix-icon="Message" size="large" />
                    </el-form-item>

                    <el-form-item label="联系方式" prop="PhoneNumber">
                        <el-input v-model="formData.PhoneNumber" placeholder="请输入联系方式" prefix-icon="Phone" size="large" />
                    </el-form-item>

                    <el-form-item label="新密码" prop="Password">
                        <el-input type="password" v-model="formData.Password" show-password placeholder="请输入新密码"
                            prefix-icon="Lock" size="large" />
                    </el-form-item>

                    <el-form-item label="验证码" prop="Code">
                        <div class="code-container">
                            <el-input v-model="formData.Code" placeholder="请输入验证码" size="large" />
                            <div class="code-image">
                                <ValidCode ref="validCodeRef" />
                            </div>
                        </div>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" class="submit-button" size="large" @click="handleForgetPassword">
                            <el-icon class="submit-icon">
                                <Check />
                            </el-icon>
                            确定重置
                        </el-button>
                    </el-form-item>
                </el-form>

                <!-- 底部链接 -->
                <div class="forget-footer">
                    <span class="back-text">已有账号？</span>
                    <RouterLink to="/Login" class="login-link">
                        返回登录
                    </RouterLink>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { Post } from '@/api/http';
import ValidCode from '@/components/Identifyingcode/ImageCode.vue';
import { ElMessage } from 'element-plus';
import { User, Lock, Message, Phone, Check } from '@element-plus/icons-vue';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

// 路由实例
const router = useRouter()

// 表单引用
const loginFormRef = ref(null)
const validCodeRef = ref(null)

// 表单数据
const formData = reactive({
    UserName: '',
    Password: '',
    Email: '',
    PhoneNumber: '',
    Code: ''
})

// 表单验证规则
const rules = {
    UserName: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 20, message: '账号长度应在3到20个字符之间', trigger: 'blur' }
    ],
    Password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/
                if (!passwordRegex.test(value)) {
                    callback(new Error('密码必须包含大小写字母和数字'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ],
    Email: [
        { required: true, message: '该项为必填项', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const reg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
                if (!value || !reg.test(value)) {
                    callback(new Error('请输入正确邮箱'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ],
    PhoneNumber: [
        { required: true, message: '该项为必填项', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                const reg = /^1[123456789]\d{9}$/
                if (!value || !reg.test(value)) {
                    callback(new Error('请输入正确的手机号'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
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
}

// 处理找回密码
const handleForgetPassword = async () => {
    if (!loginFormRef.value) return

    await loginFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                const res = await Post('/User/ForgetPassword', formData)
                if (res.Success) {
                    ElMessage.success('修改密码成功!')
                    router.push('/Login')
                } else {
                    validCodeRef.value.refreshCode()
                }
            } catch (error) {
                console.error(error)
                validCodeRef.value.refreshCode()
            }
        } else {
            ElMessage.error('验证不通过')
            validCodeRef.value.refreshCode()
        }
    })
}
</script>

<style scoped lang="scss">
/* 忘记密码页面容器 */
.forget-container {
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

/* 忘记密码卡片 */
.forget-card {
    width: 480px;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 24px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    overflow: hidden;
    position: relative;
    z-index: 1;
}

/* 忘记密码头部 */
.forget-header {
    padding: 40px 40px 20px;
    text-align: center;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
    color: white;
    position: relative;
}

.forget-header::before {
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
.forget-form-wrapper {
    padding: 40px;
}

/* 忘记密码表单 */
.forget-form {
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

/* 提交按钮 */
.submit-button {
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

.submit-icon {
    transition: transform 0.3s ease;
}

.submit-button:hover .submit-icon {
    transform: translateX(2px);
}

/* 底部链接 */
.forget-footer {
    text-align: center;
    margin-top: 24px;
}

.back-text {
    color: #6b7280;
    font-size: 14px;
}

.login-link {
    color: #10b981;
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;
    margin-left: 5px;
    transition: all 0.3s ease;

    &:hover {
        color: #059669;
        text-decoration: underline;
    }
}

/* 响应式设计 */
@media (max-width: 520px) {
    .forget-card {
        width: 90%;
        margin: 20px;
        border-radius: 16px;
    }

    .forget-header {
        padding: 30px 30px 20px;
    }

    .forget-form-wrapper {
        padding: 30px;
    }

    .main-title {
        font-size: 24px;
    }

    .health-icon {
        width: 56px;
        height: 56px;
    }

    .code-container {
        flex-direction: column;
        align-items: stretch;
        gap: 8px;
    }

    .code-image {
        align-self: center;
    }
}

@media (max-height: 700px) {
    .forget-container {
        padding: 20px 0;
    }

    .forget-header {
        padding: 30px 40px 15px;
    }

    .forget-form-wrapper {
        padding: 30px 40px;
    }
}
</style>

