<template>
    <div class="dashboard-container">
        <!-- 页面标题 -->
        <div class="page-header">
            <h1 class="page-title">📊 健康管理数据分析</h1>
            <p class="page-subtitle">全面了解系统运营状况和用户健康数据</p>
        </div>

        <!-- 概览数据卡片 -->
        <div class="overview-section" v-if="DataAnalysis.OverviewData">
            <h2 class="section-title">📈 系统概览</h2>
            <div class="overview-cards">
                <div class="overview-card">
                    <div class="card-icon">👥</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalUsers }}</div>
                        <div class="card-label">总用户数</div>
                        <div class="card-trend">💙 健康社区</div>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-icon">📚</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalHealthArticles }}</div>
                        <div class="card-label">健康文章</div>
                        <div class="card-trend">💚 知识宝库</div>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-icon">🍽️</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalRecipes }}</div>
                        <div class="card-label">健康食谱</div>
                        <div class="card-trend">🧡 营养指导</div>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-icon">🏃‍♀️</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalSports }}</div>
                        <div class="card-label">运动项目</div>
                        <div class="card-trend">❤️ 活力运动</div>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-icon">💊</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalHealthIndicators }}</div>
                        <div class="card-label">健康指标</div>
                        <div class="card-trend">💜 医疗监测</div>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-icon">📋</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalHealthRecords }}</div>
                        <div class="card-label">健康记录</div>
                        <div class="card-trend">🩵 数据档案</div>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-icon">🍎</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalDietRecords }}</div>
                        <div class="card-label">饮食记录</div>
                        <div class="card-trend">🩷 膳食管理</div>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-icon">🏋️‍♂️</div>
                    <div class="card-content">
                        <div class="card-value">{{ DataAnalysis.OverviewData.TotalSportRecords }}</div>
                        <div class="card-label">运动记录</div>
                        <div class="card-trend">💙 健身追踪</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 用户分析 -->
        <div class="user-analysis-section">
            <h2 class="section-title">👨‍👩‍👧‍👦 用户分析</h2>
            <div class="charts-row">
                <!-- 用户性别分布饼图 -->
                <div class="chart-container user-gender-chart">
                    <div class="chart-header">
                        <h3 class="chart-title gender-title">👫 用户性别分布</h3>
                        <p class="chart-desc">了解用户性别构成，优化产品设计</p>
                    </div>
                    <div class="chart-content">
                        <div ref="userGenderChart" class="chart-canvas"></div>
                    </div>
                </div>

                <!-- 用户年龄分布柱状图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">🎂 用户年龄分布</h3>
                        <p class="chart-desc">分析不同年龄段用户占比</p>
                    </div>
                    <div class="chart-content">
                        <div ref="userAgeChart" class="chart-canvas"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 健康数据分析 -->
        <div class="health-analysis-section">
            <h2 class="section-title">🏥 健康数据分析</h2>

            <!-- 健康指标趋势 -->
            <div class="chart-container full-width">
                <div class="chart-header">
                    <h3 class="chart-title">📈 健康指标记录趋势</h3>
                    <p class="chart-desc">近30天各项健康指标的记录趋势变化</p>
                </div>
                <div class="chart-content">
                    <div ref="healthIndicatorLineChart" class="chart-canvas large"></div>
                </div>
            </div>

            <div class="charts-row">
                <!-- 异常指标堆叠柱状图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">⚠️ 异常指标统计</h3>
                        <p class="chart-desc">各项健康指标的正常与异常记录对比</p>
                    </div>
                    <div class="chart-content">
                        <div ref="abnormalIndicatorChart" class="chart-canvas"></div>
                    </div>
                </div>

                <!-- 健康指标类型环形图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">🎯 健康指标类型分布</h3>
                        <p class="chart-desc">不同类型健康指标的数量分布</p>
                    </div>
                    <div class="chart-content">
                        <div ref="healthIndicatorDoughnutChart" class="chart-canvas"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 饮食运动分析 -->
        <div class="diet-sport-section">
            <h2 class="section-title">🍎🏃‍♀️ 饮食运动分析</h2>
            <div class="charts-row">
                <!-- 饮食热量面积图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">🔥 每日热量摄入趋势</h3>
                        <p class="chart-desc">近30天用户每日热量摄入变化</p>
                    </div>
                    <div class="chart-content">
                        <div ref="dietCaloriesChart" class="chart-canvas"></div>
                    </div>
                </div>

                <!-- 运动记录柱状图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">🏃‍♂️ 运动项目统计</h3>
                        <p class="chart-desc">各项运动的参与记录次数</p>
                    </div>
                    <div class="chart-content">
                        <div ref="sportRecordChart" class="chart-canvas"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 内容互动分析 -->
        <div class="engagement-section">
            <h2 class="section-title">💡 内容互动分析</h2>
            <div class="charts-row">
                <!-- 文章浏览量排行榜 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">📖 热门文章排行</h3>
                        <p class="chart-desc">最受欢迎的健康知识文章TOP10</p>
                    </div>
                    <div class="chart-content">
                        <div ref="articleViewChart" class="chart-canvas"></div>
                    </div>
                </div>

                <!-- 收藏点赞仪表盘 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">❤️ 用户互动统计</h3>
                        <p class="chart-desc">收藏、点赞等用户互动数据</p>
                    </div>
                    <div class="chart-content">
                        <div ref="engagementGaugeChart" class="chart-canvas"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 食物类型分布 -->
        <div class="food-section">
            <h2 class="section-title">🥗 食物数据分析</h2>
            <div class="charts-row">
                <!-- 食物类型分布饼图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">🍱 食物类型分布</h3>
                        <p class="chart-desc">系统中各类食物的数量分布</p>
                    </div>
                    <div class="chart-content">
                        <div ref="foodTypeChart" class="chart-canvas"></div>
                    </div>
                </div>

                <!-- 食谱审核状态漏斗图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">📝 食谱审核流程</h3>
                        <p class="chart-desc">食谱从提交到审核通过的流程统计</p>
                    </div>
                    <div class="chart-content">
                        <div ref="recipeAuditChart" class="chart-canvas"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 高级分析 -->
        <div class="advanced-section">
            <h2 class="section-title">🚀 高级分析</h2>

            <!-- 月度数据对比雷达图 -->
            <div class="chart-container full-width">
                <div class="chart-header">
                    <h3 class="chart-title">📊 月度数据对比</h3>
                    <p class="chart-desc">本月与上月各项指标的对比分析</p>
                </div>
                <div class="chart-content">
                    <div ref="monthlyRadarChart" class="chart-canvas large"></div>
                </div>
            </div>

            <div class="charts-row">
                <!-- 用户活跃度热力图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">🔥 用户活跃度热力图</h3>
                        <p class="chart-desc">一周内各时段用户活跃度分布</p>
                    </div>
                    <div class="chart-content">
                        <div ref="userActivityChart" class="chart-canvas"></div>
                    </div>
                </div>

                <!-- 健康提醒完成率散点图 -->
                <div class="chart-container">
                    <div class="chart-header">
                        <h3 class="chart-title">⏰ 健康提醒完成率</h3>
                        <p class="chart-desc">提醒次数与完成率的关系分析</p>
                    </div>
                    <div class="chart-content">
                        <div ref="healthNoticeChart" class="chart-canvas"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import defaultAvatar from '@/assets/默认头像.png'; // 导入默认头像图片
import NoImage from '@/assets/图片失效.png';
import { Post, PostSingleUpdate } from '@/api/http';
import { ref, reactive, nextTick, computed, onBeforeMount, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useCommonStore } from "@/store";
import { ElMessage, ElMessageBox } from 'element-plus';
import * as echarts from 'echarts';

const router = useRouter();
const route = useRoute();
const commonStore = useCommonStore();
const Token = computed(() => commonStore.Token);
const UserInfo = computed(() => commonStore.UserInfo);
const RoleType = computed(() => commonStore.RoleType);
const UserId = computed(() => commonStore.UserId);
const DataAnalysis = ref({});

// 图表ref引用
const userGenderChart = ref(null);
const userAgeChart = ref(null);
const healthIndicatorLineChart = ref(null);
const abnormalIndicatorChart = ref(null);
const healthIndicatorDoughnutChart = ref(null);
const dietCaloriesChart = ref(null);
const sportRecordChart = ref(null);
const articleViewChart = ref(null);
const engagementGaugeChart = ref(null);
const foodTypeChart = ref(null);
const recipeAuditChart = ref(null);
const monthlyRadarChart = ref(null);
const userActivityChart = ref(null);
const healthNoticeChart = ref(null);

// 获取数据分析接口
const DataAnalysisApi = async () => {
    const { Data } = await Post("/DataAnalysis/GetAllAnalysisData", {});
    DataAnalysis.value = Data;
    await nextTick();
    initAllCharts();
}

// 初始化所有图表
const initAllCharts = () => {
    if (!DataAnalysis.value) return;

    initUserGenderChart();
    initUserAgeChart();
    initHealthIndicatorLineChart();
    initAbnormalIndicatorChart();
    initHealthIndicatorDoughnutChart();
    initDietCaloriesChart();
    initSportRecordChart();
    initArticleViewChart();
    initEngagementGaugeChart();
    initFoodTypeChart();
    initRecipeAuditChart();
    initMonthlyRadarChart();
    initUserActivityChart();
    initHealthNoticeChart();
}

// 1. 用户性别分布饼图
const initUserGenderChart = () => {
    if (!userGenderChart.value || !DataAnalysis.value.UserGenderPieChart) return;

    const chart = echarts.init(userGenderChart.value);
    const data = DataAnalysis.value.UserGenderPieChart.map(item => ({
        name: item.Label,
        value: item.Value
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
            orient: 'horizontal',
            left: 'center',
            bottom: '10%'
        },
        series: [{
            name: '用户性别分布',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '45%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: true,
                formatter: '{b}: {d}%'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '16',
                    fontWeight: 'bold'
                }
            },
            data: data,
            color: ['#5470c6', '#91cc75', '#fac858', '#ee6666']
        }]
    };

    chart.setOption(option);

    // 响应式处理
    window.addEventListener('resize', () => chart.resize());
}

// 2. 用户年龄分布柱状图
const initUserAgeChart = () => {
    if (!userAgeChart.value || !DataAnalysis.value.UserAgeBarChart) return;

    const chart = echarts.init(userAgeChart.value);
    const data = DataAnalysis.value.UserAgeBarChart;

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: data.map(item => item.Label),
            axisTick: {
                alignWithLabel: true
            }
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            name: '用户数量',
            type: 'bar',
            barWidth: '60%',
            itemStyle: {
                borderRadius: [4, 4, 0, 0],
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#83bff6' },
                    { offset: 0.5, color: '#188df0' },
                    { offset: 1, color: '#188df0' }
                ])
            },
            data: data.map(item => item.Value)
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 3. 健康指标记录趋势线图
const initHealthIndicatorLineChart = () => {
    if (!healthIndicatorLineChart.value || !DataAnalysis.value.HealthIndicatorLineChart) return;

    const chart = echarts.init(healthIndicatorLineChart.value);
    const rawData = DataAnalysis.value.HealthIndicatorLineChart;

    // 按指标类型分组数据
    const seriesMap = {};
    rawData.forEach(item => {
        if (!seriesMap[item.Series]) {
            seriesMap[item.Series] = [];
        }
        seriesMap[item.Series].push([item.Date, item.Value]);
    });

    // 构建series数据
    const series = Object.keys(seriesMap).map((name, index) => ({
        name: name,
        type: 'line',
        smooth: true,
        data: seriesMap[name].sort((a, b) => new Date(a[0]) - new Date(b[0])),
        itemStyle: {
            color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4'][index % 8]
        }
    }));

    const option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: Object.keys(seriesMap),
            top: '5%'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'time',
            boundaryGap: false
        },
        yAxis: {
            type: 'value'
        },
        series: series
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 4. 异常指标堆叠柱状图
const initAbnormalIndicatorChart = () => {
    if (!abnormalIndicatorChart.value || !DataAnalysis.value.AbnormalIndicatorStackedChart) return;

    const chart = echarts.init(abnormalIndicatorChart.value);
    const rawData = DataAnalysis.value.AbnormalIndicatorStackedChart;

    // 获取所有指标名称
    const indicators = [...new Set(rawData.map(item => item.Label))];

    // 分别获取正常和异常数据
    const normalData = indicators.map(indicator => {
        const item = rawData.find(d => d.Label === indicator && d.Stack === '正常');
        return item ? item.Value : 0;
    });

    const abnormalData = indicators.map(indicator => {
        const item = rawData.find(d => d.Label === indicator && d.Stack === '异常');
        return item ? item.Value : 0;
    });

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['正常', '异常']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value'
        },
        yAxis: {
            type: 'category',
            data: indicators
        },
        series: [
            {
                name: '正常',
                type: 'bar',
                stack: 'total',
                itemStyle: {
                    color: '#91cc75'
                },
                data: normalData
            },
            {
                name: '异常',
                type: 'bar',
                stack: 'total',
                itemStyle: {
                    color: '#ee6666'
                },
                data: abnormalData
            }
        ]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 5. 健康指标类型环形图
const initHealthIndicatorDoughnutChart = () => {
    if (!healthIndicatorDoughnutChart.value || !DataAnalysis.value.HealthIndicatorDoughnutChart) return;

    const chart = echarts.init(healthIndicatorDoughnutChart.value);
    const data = DataAnalysis.value.HealthIndicatorDoughnutChart.map(item => ({
        name: item.Label,
        value: item.Value
    }));

    const option = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'horizontal',
            left: 'center',
            bottom: '10%'
        },
        series: [{
            name: '健康指标类型',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['50%', '45%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: false
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '14',
                    fontWeight: 'bold'
                }
            },
            data: data
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 6. 饮食热量面积图
const initDietCaloriesChart = () => {
    if (!dietCaloriesChart.value || !DataAnalysis.value.DietCaloriesAreaChart) return;

    const chart = echarts.init(dietCaloriesChart.value);
    const data = DataAnalysis.value.DietCaloriesAreaChart.map(item => [item.Date, window.ToFixed2(item.Value)]);

    const option = {
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                return `${params[0].axisValue}<br/>热量摄入: ${params[0].value[1]} 千卡`;
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'time',
            boundaryGap: false
        },
        yAxis: {
            type: 'value',
            name: '热量(千卡)'
        },
        series: [{
            name: '每日热量摄入',
            type: 'line',
            smooth: true,
            areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: 'rgba(255, 158, 68, 0.8)' },
                    { offset: 1, color: 'rgba(255, 158, 68, 0.1)' }
                ])
            },
            itemStyle: {
                color: '#ff9e44'
            },
            data: data
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 7. 运动记录柱状图
const initSportRecordChart = () => {
    if (!sportRecordChart.value || !DataAnalysis.value.SportRecordBarChart) return;

    const chart = echarts.init(sportRecordChart.value);
    const data = DataAnalysis.value.SportRecordBarChart;

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value'
        },
        yAxis: {
            type: 'category',
            data: data.map(item => item.Label)
        },
        series: [{
            name: '运动次数',
            type: 'bar',
            itemStyle: {
                borderRadius: [0, 4, 4, 0],
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                    { offset: 0, color: '#73c0de' },
                    { offset: 1, color: '#3ba272' }
                ])
            },
            data: data.map(item => item.Value)
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 8. 文章浏览量排行榜
const initArticleViewChart = () => {
    if (!articleViewChart.value || !DataAnalysis.value.ArticleViewRankingChart) return;

    const chart = echarts.init(articleViewChart.value);
    const data = DataAnalysis.value.ArticleViewRankingChart.slice(0, 10); // 只显示前10

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value'
        },
        yAxis: {
            type: 'category',
            data: data.map(item => item.Label.length > 15 ? item.Label.substring(0, 15) + '...' : item.Label),
            inverse: true
        },
        series: [{
            name: '浏览量',
            type: 'bar',
            itemStyle: {
                borderRadius: [0, 4, 4, 0],
                color: function (params) {
                    const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57'];
                    return colors[params.dataIndex % colors.length];
                }
            },
            data: data.map(item => item.Value)
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 9. 收藏点赞仪表盘
const initEngagementGaugeChart = () => {
    if (!engagementGaugeChart.value || !DataAnalysis.value.EngagementGaugeChart) return;

    const chart = echarts.init(engagementGaugeChart.value);
    const data = DataAnalysis.value.EngagementGaugeChart;

    const option = {
        series: data.map((item, index) => ({
            name: item.Label,
            type: 'gauge',
            center: [`${25 + index * 25}%`, '60%'],
            radius: '50%',
            min: 0,
            max: item.MaxValue,
            splitNumber: 5,
            axisLine: {
                lineStyle: {
                    width: 8,
                    color: [
                        [0.2, '#67e0e3'],
                        [0.8, '#37a2da'],
                        [1, '#fd666d']
                    ]
                }
            },
            pointer: {
                itemStyle: {
                    color: 'auto'
                }
            },
            axisTick: {
                distance: -20,
                length: 8,
                lineStyle: {
                    color: '#fff',
                    width: 2
                }
            },
            splitLine: {
                distance: -20,
                length: 20,
                lineStyle: {
                    color: '#fff',
                    width: 4
                }
            },
            axisLabel: {
                color: 'auto',
                distance: 30,
                fontSize: 10
            },
            detail: {
                valueAnimation: true,
                formatter: '{value}',
                color: 'auto',
                fontSize: 14,
                offsetCenter: [0, '70%']
            },
            title: {
                fontSize: 12,
                offsetCenter: [0, '90%']
            },
            data: [{
                value: item.Value,
                name: item.Label
            }]
        }))
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 10. 食物类型分布饼图
const initFoodTypeChart = () => {
    if (!foodTypeChart.value || !DataAnalysis.value.FoodTypeDistributionChart) return;

    const chart = echarts.init(foodTypeChart.value);
    const data = DataAnalysis.value.FoodTypeDistributionChart.map(item => ({
        name: item.Label,
        value: item.Value
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
            orient: 'horizontal',
            left: 'center',
            bottom: '10%'
        },
        series: [{
            name: '食物类型分布',
            type: 'pie',
            radius: '65%',
            center: ['50%', '45%'],
            itemStyle: {
                borderRadius: 6,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: true,
                formatter: '{b}: {d}%'
            },
            data: data
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 11. 食谱审核状态漏斗图
const initRecipeAuditChart = () => {
    if (!recipeAuditChart.value || !DataAnalysis.value.RecipeAuditFunnelChart) return;

    const chart = echarts.init(recipeAuditChart.value);
    const data = DataAnalysis.value.RecipeAuditFunnelChart.map(item => ({
        name: item.Label,
        value: item.Value
    }));

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}'
        },
        series: [{
            name: '食谱审核流程',
            type: 'funnel',
            left: '10%',
            top: 60,
            bottom: 60,
            width: '80%',
            min: 0,
            max: Math.max(...data.map(d => d.value)),
            minSize: '0%',
            maxSize: '100%',
            sort: 'descending',
            gap: 2,
            label: {
                show: true,
                position: 'inside'
            },
            itemStyle: {
                borderColor: '#fff',
                borderWidth: 1
            },
            emphasis: {
                label: {
                    fontSize: 20
                }
            },
            data: data
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 12. 月度数据对比雷达图
const initMonthlyRadarChart = () => {
    if (!monthlyRadarChart.value || !DataAnalysis.value.MonthlyDataRadarChart) return;

    const chart = echarts.init(monthlyRadarChart.value);
    const radarData = DataAnalysis.value.MonthlyDataRadarChart;

    const option = {
        tooltip: {},
        legend: {
            data: radarData.Datasets.map(item => item.Label),
            bottom: '10%'
        },
        radar: {
            indicator: radarData.Labels.map(label => ({
                name: label,
                max: Math.max(...radarData.Datasets.flatMap(d => d.Data)) * 1.2
            }))
        },
        series: [{
            name: '月度数据对比',
            type: 'radar',
            data: radarData.Datasets.map(dataset => ({
                value: dataset.Data,
                name: dataset.Label
            }))
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 13. 用户活跃度热力图
const initUserActivityChart = () => {
    if (!userActivityChart.value || !DataAnalysis.value.UserActivityHeatmapChart) return;

    const chart = echarts.init(userActivityChart.value);
    const data = DataAnalysis.value.UserActivityHeatmapChart;

    const hours = Array.from({ length: 24 }, (_, i) => i + '时');
    const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];

    const heatmapData = data.map(item => [item.Hour, days.indexOf(item.Day), item.Value]);

    const option = {
        tooltip: {
            position: 'top',
            formatter: function (params) {
                return `${days[params.value[1]]} ${params.value[0]}时<br/>活跃度: ${params.value[2]}`;
            }
        },
        grid: {
            height: '50%',
            top: '10%'
        },
        xAxis: {
            type: 'category',
            data: hours,
            splitArea: {
                show: true
            }
        },
        yAxis: {
            type: 'category',
            data: days,
            splitArea: {
                show: true
            }
        },
        visualMap: {
            min: 0,
            max: 100,
            calculable: true,
            orient: 'horizontal',
            left: 'center',
            bottom: '15%',
            inRange: {
                color: ['#313695', '#4575b4', '#74add1', '#abd9e9', '#e0f3f8', '#ffffbf', '#fee090', '#fdae61', '#f46d43', '#d73027', '#a50026']
            }
        },
        series: [{
            name: '用户活跃度',
            type: 'heatmap',
            data: heatmapData,
            label: {
                show: false
            },
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

// 14. 健康提醒完成率散点图
const initHealthNoticeChart = () => {
    if (!healthNoticeChart.value || !DataAnalysis.value.HealthNoticeScatterChart) return;

    const chart = echarts.init(healthNoticeChart.value);
    const data = DataAnalysis.value.HealthNoticeScatterChart.map(item => [item.X, item.Y, item.Size, item.Label]);

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: function (params) {
                return `${params.value[3]}<br/>提醒次数: ${params.value[0]}<br/>完成率: ${params.value[1]}%`;
            }
        },
        grid: {
            left: '3%',
            right: '7%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            name: '提醒次数',
            splitLine: {
                lineStyle: {
                    type: 'dashed'
                }
            }
        },
        yAxis: {
            type: 'value',
            name: '完成率(%)',
            splitLine: {
                lineStyle: {
                    type: 'dashed'
                }
            }
        },
        series: [{
            name: '健康提醒',
            type: 'scatter',
            symbolSize: function (data) {
                return Math.sqrt(data[2]) * 2;
            },
            itemStyle: {
                color: '#5470c6',
                opacity: 0.8
            },
            data: data
        }]
    };

    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
}

onBeforeMount(async () => {
    await DataAnalysisApi();
})

onMounted(() => {
    // 确保DOM已渲染完成再初始化图表
    setTimeout(() => {
        if (DataAnalysis.value && Object.keys(DataAnalysis.value).length > 0) {
            initAllCharts();
        }
    }, 100);
})

</script>
<style scoped lang="scss">
// 主容器样式
.dashboard-container {
    padding: 20px;
    // background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 50%, #f0fdf4 100%);
    min-height: 100vh;
    width: 100%;
    position: relative;

    // 健康主题装饰元素
    &::before {
        content: '';
        position: absolute;
        top: 10%;
        right: 5%;
        width: 100px;
        height: 100px;
        background: radial-gradient(circle, rgba(16, 185, 129, 0.1) 0%, transparent 70%);
        border-radius: 50%;
        z-index: 0;
    }

    &::after {
        content: '';
        position: absolute;
        bottom: 20%;
        left: 8%;
        width: 80px;
        height: 80px;
        background: radial-gradient(circle, rgba(34, 197, 94, 0.08) 0%, transparent 70%);
        border-radius: 50%;
        z-index: 0;
    }

    // 确保内容在装饰元素之上
    >* {
        position: relative;
        z-index: 1;
    }
}

// 页面头部样式
.page-header {
    text-align: center;
    margin-bottom: 30px;

    .page-title {
        font-size: 32px;
        font-weight: bold;
        color: #065f46;
        margin-bottom: 10px;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
    }

    .page-subtitle {
        font-size: 16px;
        color: #047857;
        margin: 0;
    }
}

// 区域标题样式
.section-title {
    font-size: 24px;
    font-weight: bold;
    color: #065f46;
    margin-bottom: 20px;
    padding-left: 15px;
    border-left: 4px solid #10b981;
    position: relative;

    &::after {
        content: '';
        position: absolute;
        left: -4px;
        top: 50%;
        transform: translateY(-50%);
        width: 8px;
        height: 8px;
        background: #34d399;
        border-radius: 50%;
        box-shadow: 0 0 10px rgba(52, 211, 153, 0.5);
    }
}

// 概览卡片区域
.overview-section {
    margin-bottom: 40px;

    .overview-cards {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;
        width: 100%;
    }

    .overview-card {
        background: white;
        padding: 24px;
        border-radius: 20px;
        box-shadow: 0 4px 20px rgba(16, 185, 129, 0.1);
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        border: 2px solid rgba(16, 185, 129, 0.1);
        position: relative;
        overflow: hidden;

        &:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 30px rgba(16, 185, 129, 0.15);
            border-color: rgba(16, 185, 129, 0.2);
        }

        // 健康主题装饰圆点
        &::before {
            content: '';
            position: absolute;
            top: -10px;
            right: -10px;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            opacity: 0.1;
        }

        // 小装饰圆点
        &::after {
            content: '';
            position: absolute;
            bottom: 10px;
            right: 15px;
            width: 6px;
            height: 6px;
            border-radius: 50%;
            opacity: 0.3;
        }

        // 总用户数 - 蓝色健康主题
        &:nth-child(1) {
            border-color: rgba(59, 130, 246, 0.15);
            box-shadow: 0 4px 20px rgba(59, 130, 246, 0.1);

            &::before {
                background: #3b82f6;
            }

            &::after {
                background: #60a5fa;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(59, 130, 246, 0.15);
                border-color: rgba(59, 130, 246, 0.25);
            }
        }

        // 健康文章 - 绿色知识主题
        &:nth-child(2) {
            border-color: rgba(34, 197, 94, 0.15);
            box-shadow: 0 4px 20px rgba(34, 197, 94, 0.1);

            &::before {
                background: #22c55e;
            }

            &::after {
                background: #4ade80;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(34, 197, 94, 0.15);
                border-color: rgba(34, 197, 94, 0.25);
            }
        }

        // 健康食谱 - 橙色营养主题
        &:nth-child(3) {
            border-color: rgba(251, 146, 60, 0.15);
            box-shadow: 0 4px 20px rgba(251, 146, 60, 0.1);

            &::before {
                background: #fb923c;
            }

            &::after {
                background: #fdba74;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(251, 146, 60, 0.15);
                border-color: rgba(251, 146, 60, 0.25);
            }
        }

        // 运动项目 - 红色活力主题
        &:nth-child(4) {
            border-color: rgba(239, 68, 68, 0.15);
            box-shadow: 0 4px 20px rgba(239, 68, 68, 0.1);

            &::before {
                background: #ef4444;
            }

            &::after {
                background: #f87171;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(239, 68, 68, 0.15);
                border-color: rgba(239, 68, 68, 0.25);
            }
        }

        // 健康指标 - 紫色医疗主题
        &:nth-child(5) {
            border-color: rgba(168, 85, 247, 0.15);
            box-shadow: 0 4px 20px rgba(168, 85, 247, 0.1);

            &::before {
                background: #a855f7;
            }

            &::after {
                background: #c084fc;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(168, 85, 247, 0.15);
                border-color: rgba(168, 85, 247, 0.25);
            }
        }

        // 健康记录 - 青色记录主题
        &:nth-child(6) {
            border-color: rgba(6, 182, 212, 0.15);
            box-shadow: 0 4px 20px rgba(6, 182, 212, 0.1);

            &::before {
                background: #06b6d4;
            }

            &::after {
                background: #22d3ee;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(6, 182, 212, 0.15);
                border-color: rgba(6, 182, 212, 0.25);
            }
        }

        // 饮食记录 - 粉色温馨主题
        &:nth-child(7) {
            border-color: rgba(236, 72, 153, 0.15);
            box-shadow: 0 4px 20px rgba(236, 72, 153, 0.1);

            &::before {
                background: #ec4899;
            }

            &::after {
                background: #f472b6;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(236, 72, 153, 0.15);
                border-color: rgba(236, 72, 153, 0.25);
            }
        }

        // 运动记录 - 靛蓝运动主题
        &:nth-child(8) {
            border-color: rgba(99, 102, 241, 0.15);
            box-shadow: 0 4px 20px rgba(99, 102, 241, 0.1);

            &::before {
                background: #6366f1;
            }

            &::after {
                background: #818cf8;
            }

            &:hover {
                box-shadow: 0 12px 30px rgba(99, 102, 241, 0.15);
                border-color: rgba(99, 102, 241, 0.25);
            }
        }

        .card-icon {
            font-size: 48px;
            margin-right: 20px;
            width: 72px;
            height: 72px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 16px;
            position: relative;

            // 图标发光效果
            &::before {
                content: '';
                position: absolute;
                inset: 0;
                border-radius: 16px;
                padding: 2px;
                background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.3), transparent);
                mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
                mask-composite: xor;
            }
        }

        // 图标背景色
        &:nth-child(1) .card-icon {
            background: linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(147, 197, 253, 0.1));
            color: #3b82f6;
        }

        &:nth-child(2) .card-icon {
            background: linear-gradient(135deg, rgba(34, 197, 94, 0.1), rgba(134, 239, 172, 0.1));
            color: #22c55e;
        }

        &:nth-child(3) .card-icon {
            background: linear-gradient(135deg, rgba(251, 146, 60, 0.1), rgba(253, 186, 116, 0.1));
            color: #fb923c;
        }

        &:nth-child(4) .card-icon {
            background: linear-gradient(135deg, rgba(239, 68, 68, 0.1), rgba(248, 113, 113, 0.1));
            color: #ef4444;
        }

        &:nth-child(5) .card-icon {
            background: linear-gradient(135deg, rgba(168, 85, 247, 0.1), rgba(196, 181, 253, 0.1));
            color: #a855f7;
        }

        &:nth-child(6) .card-icon {
            background: linear-gradient(135deg, rgba(6, 182, 212, 0.1), rgba(34, 211, 238, 0.1));
            color: #06b6d4;
        }

        &:nth-child(7) .card-icon {
            background: linear-gradient(135deg, rgba(236, 72, 153, 0.1), rgba(244, 114, 182, 0.1));
            color: #ec4899;
        }

        &:nth-child(8) .card-icon {
            background: linear-gradient(135deg, rgba(99, 102, 241, 0.1), rgba(129, 140, 248, 0.1));
            color: #6366f1;
        }

        .card-content {
            flex: 1;

            .card-value {
                font-size: 32px;
                font-weight: bold;
                margin-bottom: 8px;
                color: #065f46;
                text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
            }

            .card-label {
                font-size: 14px;
                color: #047857;
                font-weight: 500;
                position: relative;
                margin-bottom: 4px;

                &::before {
                    content: '●';
                    margin-right: 6px;
                    font-size: 8px;
                    opacity: 0.6;
                }
            }

            .card-trend {
                font-size: 12px;
                color: #059669;
                font-weight: 400;
                opacity: 0.8;
                font-style: italic;
            }
        }
    }
}

// 图表行布局
.charts-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
    width: 100%;
    margin-top: 20px;

}

// 图表容器样式
.chart-container {
    background: white;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(16, 185, 129, 0.08);
    overflow: hidden;
    transition: all 0.3s ease;
    border: 1px solid rgba(16, 185, 129, 0.1);
    position: relative;

    &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 3px;
        background: linear-gradient(90deg, #10b981, #34d399, #6ee7b7);
    }

    &:hover {
        box-shadow: 0 8px 30px rgba(16, 185, 129, 0.15);
        transform: translateY(-3px);
        border-color: rgba(16, 185, 129, 0.2);
    }

    &.full-width {
        grid-column: 1 / -1;
    }
}

// 图表头部
.chart-header {
    padding: 25px 20px 15px;
    border-bottom: 1px solid rgba(16, 185, 129, 0.1);
    background: linear-gradient(135deg, rgba(240, 253, 244, 0.5), rgba(236, 253, 245, 0.3));

    .chart-title {
        font-size: 18px;
        font-weight: bold;
        color: #065f46;
        margin-bottom: 8px;
        position: relative;

        &::before {
            content: '📊';
            margin-right: 8px;
            font-size: 16px;
        }

        // 特定图表类型的图标
        &.gender-title::before {
            content: '👫';
        }
    }

    .chart-desc {
        font-size: 14px;
        color: #047857;
        margin: 0;
        line-height: 1.4;
        opacity: 0.8;
    }
}

// 图表内容区域
.chart-content {
    padding: 20px;

    .chart-canvas {
        width: 100%;
        height: 300px;

        &.large {
            height: 400px;
        }
    }
}

// 各个分析区域的特殊样式
.user-analysis-section,
.health-analysis-section,
.diet-sport-section,
.engagement-section,
.food-section,
.advanced-section {
    margin-bottom: 40px;
}

// 响应式设计
@media (max-width: 768px) {
    .dashboard-container {
        padding: 15px;
    }

    .page-header .page-title {
        font-size: 24px;
    }

    .section-title {
        font-size: 20px;
    }

    .overview-cards {
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 15px;
    }

    .overview-card {
        padding: 15px;

        .card-icon {
            font-size: 36px;
            margin-right: 15px;
        }

        .card-content .card-value {
            font-size: 24px;
        }
    }

    .charts-row {
        grid-template-columns: 1fr;
        gap: 15px;
    }

    .chart-container {
        margin-bottom: 15px;
    }

    .chart-content .chart-canvas {
        height: 250px;

        &.large {
            height: 300px;
        }
    }
}

// 小屏设备优化
@media (max-width: 480px) {
    .dashboard-container {
        padding: 10px;
    }

    .overview-cards {
        grid-template-columns: 1fr;
    }

    .overview-card {
        flex-direction: column;
        text-align: center;

        .card-icon {
            margin-right: 0;
            margin-bottom: 10px;
        }
    }

    .chart-header {
        padding: 15px;
    }

    .chart-content {
        padding: 15px;

        .chart-canvas {
            height: 200px;

            &.large {
                height: 250px;
            }
        }
    }
}

// 加载动画
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.overview-section,
.user-analysis-section,
.health-analysis-section,
.diet-sport-section,
.engagement-section,
.food-section,
.advanced-section {
    animation: fadeInUp 0.6s ease-out;
}

// 滚动条样式
::-webkit-scrollbar {
    width: 6px;
    height: 6px;
}

::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
}

::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;

    &:hover {
        background: #a8a8a8;
    }
}
</style>