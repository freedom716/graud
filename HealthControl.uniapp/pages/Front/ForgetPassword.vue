<template>
    <view class="login-container">
        <!-- 健康主题背景 -->
        <view class="bg-decoration">
            <view class="health-circle health-circle-1"></view>
            <view class="health-circle health-circle-2"></view>
        </view>

        <!-- 顶部Logo和标题区域 -->
        <view class="header-section">
            <view class="logo-wrapper">
                <view class="health-icon">🩺</view>
            </view>
            <text class="app-title">重置密码</text>
            <text class="app-subtitle">找回您的账户密码</text>
        </view>

        <!-- 重置密码表单区域 -->
        <view class="form-section">
            <view class="form-container">
                <form class="login-form">
                    <!-- 用户名输入框 -->
                    <view class="form-group">
                        <view class="input-wrapper">
                            <view class="input-icon">👤</view>
                            <input type="text" v-model="formData.UserName" placeholder="请输入您的账号" class="form-input" />
                        </view>
                    </view>

                    <!-- 手机号输入框 -->
                    <view class="form-group">
                        <view class="input-wrapper">
                            <view class="input-icon">📱</view>
                            <input type="text" v-model="formData.PhoneNumber" placeholder="请输入手机号" class="form-input" />
                        </view>
                    </view>

                    <!-- 密码输入框 -->
                    <view class="form-group">
                        <view class="input-wrapper">
                            <view class="input-icon">🔒</view>
                            <input type="password" v-model="formData.Password" placeholder="请输入新密码" class="form-input"
                                password="true" />
                        </view>
                    </view>

                    <!-- 重置密码按钮 -->
                    <button class="login-button" @click="resetPassword">
                        <text class="button-text">重置密码</text>
                    </button>

                    <!-- 底部操作链接 -->
                    <view class="action-links">
                        <text class="link-text register-text" @click="toLogin">返回登录</text>
                    </view>
                </form>
            </view>
        </view>
    </view>
</template>

<script setup>
import { Post } from "@/utils/http";
import { ref } from 'vue';

// 响应式数据
const formData = ref({
    UserName: "",
    Password: "",
    PhoneNumber: "",
});

// 重置密码方法
const resetPassword = async () => {
    if (!formData.value.UserName) {
        uni.showToast({
            title: "请输入账户",
            icon: "none"
        })
        return;
    }

    if (!formData.value.PhoneNumber) {
        uni.showToast({
            title: "请输入手机号",
            icon: "none"
        })
        return;
    }

    if (!formData.value.Password) {
        uni.showToast({
            title: "请输入密码",
            icon: "none"
        })
        return;
    }

    const { Data, Success } = await Post("/User/ForgetPassword", formData.value);
    if (Success) {
        uni.showToast({
            title: "密码重置成功",
            icon: "success"
        });
        uni.redirectTo({
            url: "/pages/Front/Login"
        });
    }
};

// 去登录
const toLogin = () => {
    uni.redirectTo({
        url: "/pages/Front/Login"
    });
};
</script>

<style scoped>
/* 全局页面样式 */
page {
    background-color: #f8fffe;
}

/* 登录容器 - 健康主题背景 */
.login-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #e8f8f5 0%, #f0fdf4 50%, #ffffff 100%);
    position: relative;
    overflow: hidden;
}

/* 健康主题背景装饰 */
.bg-decoration {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 0;
}

.health-circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.08;
    background: linear-gradient(45deg, #10b981, #34d399);
}

.health-circle-1 {
    width: 500rpx;
    height: 500rpx;
    top: -150rpx;
    right: -150rpx;
    animation: gentle-float 15s ease-in-out infinite;
}

.health-circle-2 {
    width: 300rpx;
    height: 300rpx;
    bottom: -100rpx;
    left: -100rpx;
    animation: gentle-float 20s ease-in-out infinite reverse;
}

/* 顶部标题区域 */
.header-section {
    padding: 120rpx 0 80rpx;
    text-align: center;
    position: relative;
    z-index: 1;
}

.logo-wrapper {
    margin-bottom: 40rpx;
}

.health-icon {
    font-size: 120rpx;
    background: linear-gradient(135deg, #10b981, #34d399);
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    filter: drop-shadow(0 4rpx 8rpx rgba(16, 185, 129, 0.2));
}

.app-title {
    display: block;
    font-size: 48rpx;
    font-weight: 600;
    color: #065f46;
    margin-bottom: 16rpx;
    letter-spacing: 2rpx;
}

.app-subtitle {
    display: block;
    font-size: 28rpx;
    color: #6b7280;
    opacity: 0.8;
}

/* 表单区域 */
.form-section {
    position: relative;
    z-index: 1;
    padding: 0 40rpx;
}

.form-container {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 24rpx;
    padding: 60rpx 40rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.06);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.8);
}

/* 表单样式 */
.login-form {
    width: 100%;
}

.form-group {
    margin-bottom: 40rpx;
}

.input-wrapper {
    display: flex;
    align-items: center;
    background: #f9fafb;
    border: 1px solid #e5e7eb;
    border-radius: 12rpx;
    padding: 0 24rpx;
    transition: all 0.3s ease;
}

.input-wrapper:focus-within {
    border-color: #10b981;
    background: #ffffff;
    box-shadow: 0 0 0 3rpx rgba(16, 185, 129, 0.1);
}

.input-icon {
    font-size: 32rpx;
    color: #6b7280;
    margin-right: 16rpx;
}

.form-input {
    flex: 1;
    height: 88rpx;
    font-size: 32rpx;
    color: #374151;
    background: transparent;
    border: none;
}

.form-input::placeholder {
    color: #9ca3af;
}

/* 重置密码按钮 */
.login-button {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #10b981, #34d399);
    border: none;
    border-radius: 44rpx;
    margin: 40rpx 0 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(16, 185, 129, 0.3);
    transition: all 0.3s ease;
}

.login-button:active {
    transform: translateY(2rpx);
    box-shadow: 0 4rpx 12rpx rgba(16, 185, 129, 0.2);
}

.button-text {
    color: #ffffff;
    font-size: 32rpx;
    font-weight: 600;
    letter-spacing: 2rpx;
}

/* 底部操作链接 */
.action-links {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 30rpx;
    padding: 0 8rpx;
}

.link-text {
    font-size: 28rpx;
    color: #6b7280;
    transition: color 0.3s ease;
}

.register-text {
    color: #10b981;
    font-weight: 500;
}

.link-text:active {
    color: #10b981;
}

/* 动画效果 */
@keyframes gentle-float {
    0%,
    100% {
        transform: translateY(0) rotate(0deg);
    }
    50% {
        transform: translateY(-30rpx) rotate(3deg);
    }
}

/* 响应式适配 */
@media (max-width: 750rpx) {
    .form-container {
        margin: 0 20rpx;
        padding: 50rpx 30rpx;
    }

    .header-section {
        padding: 100rpx 0 60rpx;
    }

    .health-icon {
        font-size: 100rpx;
    }

    .app-title {
        font-size: 42rpx;
    }
}
</style>