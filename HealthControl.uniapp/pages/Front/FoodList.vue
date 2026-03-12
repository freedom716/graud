<template>
    <view class="food-list-container">
        <!-- 导航栏 -->
        <uni-nav-bar dark :fixed="true" shadow background-color="#4CAF50" status-bar left-icon="left" left-text="返回"
            @clickLeft="goBack" title="🥗 健康食物库" />

        <!-- 主要内容区域 -->
        <view class="content-wrapper">
            <!-- 左侧分类菜单 -->
            <view class="category-menu">
                <scroll-view class="category-scroll" scroll-y>
                    <view v-for="(category, index) in FoodTypeList" :key="category.Id" class="category-item"
                        :class="{ active: activeCategory === index }" @click="selectCategory(index, category.Id)">
                        <text class="category-name">{{ category.Name }}</text>
                    </view>
                </scroll-view>
            </view>

            <!-- 右侧食物列表 -->
            <view class="food-content">
                <scroll-view class="food-scroll" scroll-y @scroll="onFoodScroll" :scroll-top="scrollTop"
                    :scroll-with-animation="true">
                    <view v-for="category in FoodTypeList" :key="category.Id" :id="`category-${category.Id}`"
                        class="food-category-section">
                        <!-- 分类标题 -->
                        <view class="category-title">
                            <text class="title-text">{{ category.Name }}</text>
                        </view>

                        <!-- 该分类下的食物列表 -->
                        <view class="food-list">
                            <view v-for="food in category.Foods" :key="food.Id" class="food-item" @click="selectFood(food)">
                                <!-- 食物图片 -->
                                <view class="food-image">
                                    <image :src="food.Cover" mode="aspectFill" class="food-cover" />
                                </view>

                                <!-- 食物信息 -->
                                <view class="food-info">
                                    <view class="food-name">{{ food.Name }}</view>
                                    <view class="food-nutrition">
                                        <text class="nutrition-item">热量: {{ food.Calories }}kcal/1g</text>
                                        <text class="nutrition-item">蛋白质: {{ food.Protein }}g</text>
                                        <text class="nutrition-item">碳水: {{ food.Carbohydrates }}g</text>
                                        <text class="nutrition-item">脂肪: {{ food.Fat }}g</text>
                                    </view>

                                    <!-- 食物单位选择 -->
                                    <view class="food-units" v-if="food.FoodUnits && food.FoodUnits.length > 0">
                                        <text class="units-label">常见单位：</text>
                                        <view class="units-list">
                                            <view v-for="unit in food.FoodUnits" :key="unit.Id" class="unit-item"
                                                @click.stop="selectUnit(food, unit)">
                                                <text class="unit-name">{{ unit.UnitName }}</text>
                                                <text class="unit-calories">({{ unit.Calories }}kcal)</text>
                                            </view>
                                        </view>
                                    </view>
                                </view>
                            </view>
                        </view>
                    </view>
                </scroll-view>
            </view>
        </view>

        <!-- 分量输入弹窗 -->
        <uni-popup ref="portionPopup" type="center" background-color="rgba(0,0,0,0.5)">
            <view class="portion-input-popup" v-if="selectedUnit">
                <view class="portion-header">
                    <text class="portion-title">🍽️ 添加食物记录</text>
                    <view class="portion-close" @click="closePortionPopup">
                        <uni-icons type="closeempty" size="24" color="#666"></uni-icons>
                    </view>
                </view>

                <view class="portion-content">
                    <!-- 食物信息显示 -->
                    <view class="food-summary">
                        <image :src="selectedUnit.food.Cover" mode="aspectFill" class="summary-image" />
                        <view class="summary-info">
                            <text class="summary-name">{{ selectedUnit.food.Name }}</text>
                            <text class="summary-unit">单位: {{ selectedUnit.unit.UnitName }} ({{ selectedUnit.unit.UnitValue
                            }}g)</text>
                        </view>
                    </view>

                    <!-- 分量输入 -->
                    <view class="input-section">
                        <text class="input-label">🥄 请输入分量</text>
                        <view class="input-wrapper">
                            <uni-easyinput v-model="portionAmount" type="number"
                                :placeholder="`请输入${selectedUnit.unit.UnitName}数量`" :styles="inputStyles"
                                @input="calculateNutrition" />
                            <text class="input-unit">{{ selectedUnit.unit.UnitName }}</text>
                        </view>
                    </view>

                    <!-- 计算后的营养信息 -->
                    <view class="calculated-nutrition" v-if="calculatedNutrition">
                        <text class="nutrition-title">📊 营养成分预览</text>
                        <view class="nutrition-cards">
                            <view class="nutrition-card">
                                <text class="card-label">热量</text>
                                <text class="card-value">{{ calculatedNutrition.calories }}kcal</text>
                            </view>
                            <view class="nutrition-card">
                                <text class="card-label">蛋白质</text>
                                <text class="card-value">{{ calculatedNutrition.protein }}g</text>
                            </view>
                            <view class="nutrition-card">
                                <text class="card-label">碳水</text>
                                <text class="card-value">{{ calculatedNutrition.carbs }}g</text>
                            </view>
                            <view class="nutrition-card">
                                <text class="card-label">脂肪</text>
                                <text class="card-value">{{ calculatedNutrition.fat }}g</text>
                            </view>
                        </view>
                    </view>

                    <!-- 时间选择 -->
                    <view class="time-section">
                        <text class="time-label">⏰ 记录时间</text>
                        <uni-datetime-picker v-model="recordTime" type="datetime" :clear-icon="false" :border="false"
                            placeholder="选择记录时间">
                            <view class="time-display">
                                <text class="time-text">{{ formatTime(recordTime) }}</text>
                                <uni-icons type="calendar" size="20" color="#4CAF50"></uni-icons>
                            </view>
                        </uni-datetime-picker>
                    </view>

                    <!-- 操作按钮 -->
                    <view class="action-buttons">
                        <view class="cancel-btn" @click="closePortionPopup">
                            <text class="btn-text">取消</text>
                        </view>
                        <view class="save-btn" @click="saveDietRecord" :class="{ disabled: !canSave }">
                            <text class="btn-text">💾 保存记录</text>
                        </view>
                    </view>
                </view>
            </view>
        </uni-popup>

        <!-- 食物选择弹窗 -->
        <uni-popup ref="foodPopup" type="bottom" background-color="#f8fdf8">
            <view class="food-detail-popup" v-if="selectedFood">
                <view class="popup-header">
                    <text class="popup-title">{{ selectedFood.Name }}</text>
                    <view class="close-btn" @click="closeFoodPopup">
                        <uni-icons type="closeempty" size="24" color="#999"></uni-icons>
                    </view>
                </view>

                <view class="popup-content">
                    <image :src="selectedFood.Cover" mode="aspectFill" class="popup-image" />

                    <view class="nutrition-detail">
                        <text class="detail-title">营养成分 (每1g)</text>
                        <view class="nutrition-grid">
                            <view class="nutrition-cell">
                                <text class="cell-label">热量</text>
                                <text class="cell-value">{{ selectedFood.Calories }}kcal</text>
                            </view>
                            <view class="nutrition-cell">
                                <text class="cell-label">蛋白质</text>
                                <text class="cell-value">{{ selectedFood.Protein }}g</text>
                            </view>
                            <view class="nutrition-cell">
                                <text class="cell-label">碳水化合物</text>
                                <text class="cell-value">{{ selectedFood.Carbohydrates }}g</text>
                            </view>
                            <view class="nutrition-cell">
                                <text class="cell-label">脂肪</text>
                                <text class="cell-value">{{ selectedFood.Fat }}g</text>
                            </view>
                        </view>
                    </view>

                    <view class="unit-selection" v-if="selectedFood.FoodUnits && selectedFood.FoodUnits.length > 0">
                        <text class="detail-title">选择单位</text>
                        <view class="unit-options">
                            <view v-for="unit in selectedFood.FoodUnits" :key="unit.Id" class="unit-option"
                                @click="confirmSelectUnit(selectedFood, unit)">
                                <view class="unit-info">
                                    <text class="unit-main">{{ unit.UnitName }}</text>
                                    <text class="unit-weight">({{ unit.UnitValue }}g)</text>
                                </view>
                                <view class="unit-nutrition">
                                    <text class="unit-cal">{{ unit.Calories }}kcal</text>
                                </view>
                            </view>
                        </view>
                    </view>
                </view>
            </view>
        </uni-popup>
    </view>
</template>

<script setup>
import { useCommonStore } from '@/store';
import { Post } from '@/utils/http';
import { onLoad, onReady, onShow } from "@dcloudio/uni-app";
import { computed, reactive, ref, nextTick } from 'vue';
import { GetFormatFullDate } from '@/utils/comm';

// 获取store
const commonStore = useCommonStore();
const Token = computed(() => commonStore.Token)
const UserInfo = computed(() => commonStore.UserInfo)
const RoleType = computed(() => commonStore.RoleType)
const UserId = computed(() => commonStore.UserId)

// 响应式数据
const FoodTypeList = ref([]);
const activeCategory = ref(0); // 当前激活的分类索引
const scrollTop = ref(0); // 右侧滚动位置
const selectedFood = ref(null); // 选中的食物
const foodPopup = ref(null); // 弹窗引用

// 分量输入相关数据
const portionPopup = ref(null); // 分量输入弹窗引用
const selectedUnit = ref(null); // 选中的单位信息 { food, unit }
const portionAmount = ref(''); // 输入的分量数量
const recordTime = ref(new Date()); // 记录时间，默认当前时间
const calculatedNutrition = ref(null); // 计算后的营养信息

// 输入框样式
const inputStyles = {
    borderColor: '#4CAF50',
    borderRadius: '12rpx'
};

const where = reactive({});

// 计算属性
const canSave = computed(() => {
    return portionAmount.value && parseFloat(portionAmount.value) > 0 && selectedUnit.value;
});

// 生命周期钩子
onLoad(async (option) => {
});

onShow(async () => {
    await GetFoodTypeListApi();
});

onReady(async () => {
});

// 方法
const goBack = () => {
    uni.navigateBack();
};

// 获取食物分类列表
const GetFoodTypeListApi = async () => {
    let {
        Data: {
            Items
        }
    } = await Post('/FoodType/List', { isQueryChild: true });
    FoodTypeList.value = Items;
};

// 选择分类
const selectCategory = async (index, categoryId) => {
    activeCategory.value = index;

    // 滚动到对应的分类区域
    await nextTick();

    // 获取目标元素位置并滚动
    const query = uni.createSelectorQuery();
    query.select(`#category-${categoryId}`).boundingClientRect();
    query.selectViewport().scrollOffset();
    query.exec((res) => {
        if (res[0]) {
            scrollTop.value = res[0].top - 100; // 减去一些偏移量
        }
    });
};

// 监听右侧滚动，同步左侧菜单
const onFoodScroll = (e) => {
    const scrollTop = e.detail.scrollTop;

    // 获取所有分类区域的位置，找到当前应该高亮的分类
    FoodTypeList.value.forEach((category, index) => {
        const query = uni.createSelectorQuery();
        query.select(`#category-${category.Id}`).boundingClientRect();
        query.exec((res) => {
            if (res[0]) {
                const categoryTop = res[0].top;
                const categoryBottom = res[0].top + res[0].height;

                // 如果当前滚动位置在这个分类区域内，就高亮这个分类
                if (scrollTop >= categoryTop - 200 && scrollTop < categoryBottom - 200) {
                    activeCategory.value = index;
                }
            }
        });
    });
};

// 选择食物
const selectFood = (food) => {
    selectedFood.value = food;
    foodPopup.value.open();
};

// 关闭食物详情弹窗
const closeFoodPopup = () => {
    foodPopup.value.close();
    selectedFood.value = null;
};

// 选择食物单位（在列表中直接选择）
const selectUnit = (food, unit) => {
    selectedUnit.value = { food, unit };
    portionAmount.value = '1'; // 默认数量为1
    recordTime.value = new Date(); // 重置为当前时间
    calculatedNutrition.value = null;

    // 计算默认营养信息
    calculateNutrition();

    // 打开分量输入弹窗
    portionPopup.value.open();
};

// 确认选择单位（在弹窗中选择）
const confirmSelectUnit = (food, unit) => {
    // 关闭食物详情弹窗
    closeFoodPopup();

    // 打开分量输入弹窗
    selectUnit(food, unit);
};

// 关闭分量输入弹窗
const closePortionPopup = () => {
    portionPopup.value.close();
    selectedUnit.value = null;
    portionAmount.value = '';
    calculatedNutrition.value = null;
};

// 计算营养信息
const calculateNutrition = () => {
    if (!selectedUnit.value || !portionAmount.value) {
        calculatedNutrition.value = null;
        return;
    }

    const amount = parseFloat(portionAmount.value);
    if (isNaN(amount) || amount <= 0) {
        calculatedNutrition.value = null;
        return;
    }

    const { unit } = selectedUnit.value;

    // 根据输入的分量计算营养成分
    // unit中的营养成分已经是按照UnitValue计算好的总量，直接乘以用户输入的分量即可
    calculatedNutrition.value = {
        calories: Math.round(unit.Calories * amount * 100) / 100,
        protein: Math.round(unit.Protein * amount * 100) / 100,
        carbs: Math.round(unit.Carbohydrates * amount * 100) / 100,
        fat: Math.round(unit.Fat * amount * 100) / 100
    };
};

// 格式化时间显示
const formatTime = (time) => {
    if (!time) return '选择时间';

    const date = new Date(time);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
};

// 保存饮食记录
const saveDietRecord = async () => {
    if (!canSave.value) {
        uni.showToast({
            title: '请输入有效的分量',
            icon: 'none'
        });
        return;
    }

    try {
        uni.showLoading({
            title: '保存中...'
        });

        const { food, unit } = selectedUnit.value;
        const amount = parseInt(portionAmount.value);

        // 构建请求数据，对应后端DietRecord实体
        const dietRecordData = {
            FoodId: food.Id,
            RecordUserId: UserId.value, // 使用当前用户ID
            FoodUnitId: unit.Id,
            RecordTime: GetFormatFullDate(new Date(recordTime.value)),
            RecordValue: amount
        };

        // 调用后端API保存饮食记录
        const result = await Post('/DietRecord/CreateOrEdit', dietRecordData);

        uni.hideLoading();

        if (result.Success) {
            uni.showToast({
                title: '记录保存成功！',
                icon: 'success'
            });

            // 关闭弹窗
            closePortionPopup();
        } else {
            uni.showToast({
                title: result.Msg || '保存失败',
                icon: 'none'
            });
        }

    } catch (error) {
        uni.hideLoading();
        uni.showToast({
            title: '网络错误，请重试',
            icon: 'none'
        });
        console.error('保存饮食记录失败:', error);
    }
};
</script>

<style scoped lang="scss">
.food-list-container {
    height: 100vh;
    background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
}

/* 主要内容区域 */
.content-wrapper {
    display: flex;
    height: calc(100vh - 44px);

}

/* 左侧分类菜单 */
.category-menu {
    width: 120rpx;
    background: linear-gradient(180deg, #ffffff 0%, #f8fdf8 100%);
    border-right: 1rpx solid #e0f0e0;
    box-shadow: 2rpx 0 8rpx rgba(76, 175, 80, 0.1);

    .category-scroll {
        height: 100%;
    }

    .category-item {
        height: 100rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-bottom: 1rpx solid #f0f0f0;
        background-color: #fff;
        transition: all 0.3s;

        &.active {
            background: linear-gradient(90deg, #e8f5e8 0%, #d4f4d4 100%);
            border-right: 4rpx solid #4CAF50;
            box-shadow: inset -2rpx 0 4rpx rgba(76, 175, 80, 0.2);

            .category-name {
                color: #2E7D32;
                font-weight: bold;
            }
        }

        .category-name {
            font-size: 24rpx;
            color: #333;
            text-align: center;
            line-height: 1.2;
        }
    }
}

/* 右侧食物内容 */
.food-content {
    flex: 1;

    .food-scroll {
        height: 100%;
        padding: 0 20rpx;
    }
}

/* 分类区域 */
.food-category-section {
    margin-bottom: 40rpx;

    .category-title {
        position: sticky;
        top: 0;
        background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
        padding: 20rpx 0;
        z-index: 10;
        border-radius: 12rpx 12rpx 0 0;
        margin-bottom: 8rpx;

        .title-text {
            font-size: 32rpx;
            font-weight: bold;
            color: #2E7D32;
            display: flex;
            align-items: center;

            &::before {
                content: '🌿';
                margin-right: 12rpx;
                font-size: 28rpx;
            }
        }
    }
}

/* 食物列表 */
.food-list {
    background: linear-gradient(135deg, #ffffff 0%, #fafffe 100%);
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 4rpx 12rpx rgba(76, 175, 80, 0.1);
    border: 1rpx solid rgba(76, 175, 80, 0.1);
}

/* 食物项 */
.food-item {
    display: flex;
    padding: 24rpx;
    border-bottom: 1rpx solid #f0f0f0;
    transition: background-color 0.2s;

    &:last-child {
        border-bottom: none;
    }

    &:active {
        background: linear-gradient(135deg, #f0f8f0 0%, #e8f5e8 100%);
    }

    .food-image {
        width: 120rpx;
        height: 120rpx;
        margin-right: 24rpx;

        .food-cover {
            width: 100%;
            height: 100%;
            border-radius: 12rpx;
        }
    }

    .food-info {
        flex: 1;

        .food-name {
            font-size: 32rpx;
            font-weight: bold;
            color: #2E7D32;
            margin-bottom: 12rpx;
            display: flex;
            align-items: center;

            &::after {
                content: '🍃';
                margin-left: 8rpx;
                font-size: 20rpx;
                opacity: 0.7;
            }
        }

        .food-nutrition {
            margin-bottom: 16rpx;

            .nutrition-item {
                display: inline-block;
                font-size: 24rpx;
                color: #558B2F;
                margin-right: 16rpx;
                margin-bottom: 8rpx;
                background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
                padding: 4rpx 12rpx;
                border-radius: 12rpx;
                border: 1rpx solid rgba(76, 175, 80, 0.2);

                &::before {
                    content: '💚';
                    margin-right: 6rpx;
                    font-size: 16rpx;
                }
            }
        }

        .food-units {
            .units-label {
                font-size: 24rpx;
                color: #999;
                margin-bottom: 8rpx;
            }

            .units-list {
                display: flex;
                flex-wrap: wrap;
                gap: 12rpx;

                .unit-item {
                    background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
                    border: 1rpx solid #4CAF50;
                    border-radius: 8rpx;
                    padding: 8rpx 16rpx;
                    transition: all 0.2s;

                    &:active {
                        background: linear-gradient(135deg, #d4f4d4 0%, #e0f0e0 100%);
                        transform: scale(0.98);
                        box-shadow: 0 2rpx 4rpx rgba(76, 175, 80, 0.3);
                    }

                    .unit-name {
                        font-size: 22rpx;
                        color: #2E7D32;
                        margin-right: 8rpx;
                        font-weight: 600;
                    }

                    .unit-calories {
                        font-size: 20rpx;
                        color: #999;
                    }
                }
            }
        }
    }
}

/* 分量输入弹窗样式 */
.portion-input-popup {
    width: 600rpx;
    background: linear-gradient(135deg, #f8fdf8 0%, #ffffff 100%);
    border-radius: 24rpx;
    box-shadow: 0 8rpx 32rpx rgba(76, 175, 80, 0.2);

    .portion-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 32rpx;
        border-bottom: 1rpx solid #e0f0e0;
        background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
        border-radius: 24rpx 24rpx 0 0;

        .portion-title {
            font-size: 32rpx;
            font-weight: bold;
            color: #2E7D32;
        }

        .portion-close {
            padding: 8rpx;
            background: rgba(255, 255, 255, 0.8);
            border-radius: 50%;
            transition: all 0.2s;

            &:active {
                background: rgba(76, 175, 80, 0.1);
                transform: scale(0.95);
            }
        }
    }

    .portion-content {
        padding: 32rpx;

        .food-summary {
            display: flex;
            align-items: center;
            margin-bottom: 32rpx;
            padding: 20rpx;
            background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
            border-radius: 16rpx;
            border: 1rpx solid rgba(76, 175, 80, 0.2);

            .summary-image {
                width: 80rpx;
                height: 80rpx;
                border-radius: 12rpx;
                margin-right: 20rpx;
            }

            .summary-info {
                flex: 1;

                .summary-name {
                    display: block;
                    font-size: 28rpx;
                    font-weight: bold;
                    color: #2E7D32;
                    margin-bottom: 8rpx;
                }

                .summary-unit {
                    font-size: 24rpx;
                    color: #558B2F;
                }
            }
        }

        .input-section {
            margin-bottom: 32rpx;

            .input-label {
                display: block;
                font-size: 28rpx;
                font-weight: bold;
                color: #2E7D32;
                margin-bottom: 16rpx;
            }

            .input-wrapper {
                display: flex;
                align-items: center;

                .input-unit {
                    margin-left: 16rpx;
                    font-size: 24rpx;
                    color: #558B2F;
                    font-weight: 600;
                    background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
                    padding: 12rpx 20rpx;
                    border-radius: 12rpx;
                    border: 1rpx solid rgba(76, 175, 80, 0.3);
                }
            }
        }

        .calculated-nutrition {
            margin-bottom: 32rpx;

            .nutrition-title {
                display: block;
                font-size: 28rpx;
                font-weight: bold;
                color: #2E7D32;
                margin-bottom: 16rpx;
            }

            .nutrition-cards {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 12rpx;

                .nutrition-card {
                    background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
                    padding: 16rpx;
                    border-radius: 12rpx;
                    text-align: center;
                    border: 1rpx solid rgba(76, 175, 80, 0.2);

                    .card-label {
                        display: block;
                        font-size: 22rpx;
                        color: #558B2F;
                        margin-bottom: 8rpx;
                    }

                    .card-value {
                        font-size: 24rpx;
                        font-weight: bold;
                        color: #2E7D32;
                    }
                }
            }
        }

        .time-section {
            margin-bottom: 32rpx;

            .time-label {
                display: block;
                font-size: 28rpx;
                font-weight: bold;
                color: #2E7D32;
                margin-bottom: 16rpx;
            }

            .time-display {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 20rpx;
                background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
                border-radius: 12rpx;
                border: 1rpx solid rgba(76, 175, 80, 0.3);

                .time-text {
                    font-size: 26rpx;
                    color: #2E7D32;
                }
            }
        }

        .action-buttons {
            display: flex;
            gap: 16rpx;

            .cancel-btn,
            .save-btn {
                flex: 1;
                height: 80rpx;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 16rpx;
                transition: all 0.2s;

                .btn-text {
                    font-size: 28rpx;
                    font-weight: 600;
                }
            }

            .cancel-btn {
                background: linear-gradient(135deg, #f5f5f5 0%, #e0e0e0 100%);
                border: 1rpx solid #ccc;

                .btn-text {
                    color: #666;
                }

                &:active {
                    transform: scale(0.98);
                    background: linear-gradient(135deg, #e0e0e0 0%, #d0d0d0 100%);
                }
            }

            .save-btn {
                background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
                border: 1rpx solid #4CAF50;

                .btn-text {
                    color: #fff;
                }

                &:active {
                    transform: scale(0.98);
                    background: linear-gradient(135deg, #388E3C 0%, #4CAF50 100%);
                }

                &.disabled {
                    background: linear-gradient(135deg, #ccc 0%, #999 100%);
                    border-color: #ccc;
                    opacity: 0.6;

                    .btn-text {
                        color: #666;
                    }
                }
            }
        }
    }
}

/* 食物详情弹窗样式 */
.food-detail-popup {
    max-height: 70vh;
    background: linear-gradient(135deg, #f8fdf8 0%, #ffffff 100%);
    border-radius: 24rpx 24rpx 0 0;

    .popup-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 32rpx;
        border-bottom: 1rpx solid #e0f0e0;
        background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
        border-radius: 24rpx 24rpx 0 0;

        .popup-title {
            font-size: 36rpx;
            font-weight: bold;
            color: #2E7D32;
            display: flex;
            align-items: center;

            &::before {
                content: '🌱';
                margin-right: 12rpx;
                font-size: 32rpx;
            }
        }

        .close-btn {
            padding: 8rpx;
            background: rgba(255, 255, 255, 0.8);
            border-radius: 50%;
            transition: all 0.2s;

            &:active {
                background: rgba(76, 175, 80, 0.1);
                transform: scale(0.95);
            }
        }
    }

    .popup-content {
        padding: 32rpx;

        .popup-image {
            width: 100%;
            height: 300rpx;
            border-radius: 16rpx;
            margin-bottom: 32rpx;
        }

        .nutrition-detail {
            margin-bottom: 32rpx;

            .detail-title {
                font-size: 28rpx;
                font-weight: bold;
                color: #2E7D32;
                margin-bottom: 16rpx;
                display: flex;
                align-items: center;

                &::before {
                    content: '📊';
                    margin-right: 12rpx;
                    font-size: 24rpx;
                }
            }

            .nutrition-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 16rpx;

                .nutrition-cell {
                    background: linear-gradient(135deg, #e8f5e8 0%, #f0f8f0 100%);
                    padding: 20rpx;
                    border-radius: 12rpx;
                    text-align: center;
                    border: 1rpx solid rgba(76, 175, 80, 0.2);
                    box-shadow: 0 2rpx 8rpx rgba(76, 175, 80, 0.1);

                    .cell-label {
                        display: block;
                        font-size: 24rpx;
                        color: #558B2F;
                        margin-bottom: 8rpx;
                        font-weight: 500;
                    }

                    .cell-value {
                        font-size: 28rpx;
                        font-weight: bold;
                        color: #2E7D32;
                    }
                }
            }
        }

        .unit-selection {
            .detail-title {
                font-size: 28rpx;
                font-weight: bold;
                color: #333;
                margin-bottom: 16rpx;
            }

            .unit-options {
                .unit-option {
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    padding: 24rpx;
                    background-color: #f8f9fa;
                    border-radius: 12rpx;
                    margin-bottom: 16rpx;

                    &:last-child {
                        margin-bottom: 0;
                    }

                    .unit-info {
                        .unit-main {
                            font-size: 28rpx;
                            font-weight: bold;
                            color: #333;
                            margin-right: 12rpx;
                        }

                        .unit-weight {
                            font-size: 24rpx;
                            color: #666;
                        }
                    }

                    .unit-nutrition {
                        .unit-cal {
                            font-size: 24rpx;
                            color: #007aff;
                            font-weight: bold;
                        }
                    }
                }
            }
        }
    }
}
</style>