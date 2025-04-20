<template>
  <div class="line-main">
    <div>
      <span class="tag">{{ tag }}</span>
      <span class="time-show">
        <span class="top-bar" style="font-size: 12px;">時間選擇</span>
        <el-select size="mini" style="width: 90px;" v-model="selectedValue" placeholder="期限">
          <el-option v-for="item in options" :key="item.num" :label="item.name" :value="item.num">
          </el-option>
        </el-select>
      </span>
    </div>
    <div ref="chart" :style="{ width: '100%', height: height }"></div>
  </div>
</template>
<script>
// 折線圖組件
import * as echarts from 'echarts';
export default {
  name: 'DialogLine',
  props: {
    // 圖表標題
    tag: {
      type: String,
      default: '折線圖'
    },
    // 圖表的數值資料
    values: {
      type: Array,
      required: true
    },
    // 圖表的時間軸資料
    date: {
      type: Array,
      required: true
    },
    // 圖表高度
    height: {
      type: String,
      default: '220px'
    },
  },
  watch: {
    selectedValue(v1, v2) { // 通知父層使用者切換了時間
      this.$emit('on-selected', v1);
    },
    values(v1, v2) { // 每次資料改變時重畫圖表
      this.initChart();
    }
  },
  data() {
    return {
      chart: null,// echarts 實例
      options: [{ num: 7, name: '7天內' }, { num: 30, name: '30天內' }, { num: 60, name: '60天內' }],// 可選擇的天數區間
      selectedValue: '',// 使用者選擇的區間
    }
  },
  methods: {
    // 圖表初始化
    initChart() {
      this.chart = echarts.init(this.$refs.chart);
      let option = {
        grid: {  // 圖表邊界
          left: 30,
          right: 10,
          top: 50,
          borderWidth: 0,
        },
        title: { 
          text: '',
          color: '#ffffff',
        },
        tooltip: { // 提示框設定
          trigger: 'axis',
          formatter: '{b}{c}',
        },
        legend: {
          data: ['']
        },
        xAxis: { // X 軸是時間資料
          data: this.date,
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: {
            color: 'rgb(102, 102, 102)',
            fontSize: '12'
          },
        },
        yAxis: { // Y 軸數值
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: {
            color: 'rgb(102, 102, 102)',
            fontSize: '12',
            formatter: '{value}'
          },
        },
        series: [{
          name: '',
          type: 'line',
          smooth: true, // 平滑折線
          data: this.values, // 對應傳入的資料
          areaStyle: {
            color: 'rgba(144, 191, 237, 0.3)'
          },
          lineStyle: {
            color: '#5B8FF9'
          },
          itemStyle: {
            color: '#5B8FF9',
            borderColor: '#5B8FF9',
            borderWidth: 2
          },
          label: {
            show: true,
            position: 'top',
            color: 'rgb(102, 102, 102)',
          },
        }]
      };
      this.chart.setOption(option);
    },
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose(); // 銷毀圖表資源，避免 memory leak
  },
};  
</script>
<style scoped lang="scss">
.line-main {
  margin-bottom: 5px;
  border-radius: 3px;

  .tag {
    font-size: 16px;
    padding: 15px 6px;
    display: inline-block;
    color: #333;
    font-weight: bold;
  }

  .time-show {
    padding: 15px 6px;
    float: right;
  }

}
</style>