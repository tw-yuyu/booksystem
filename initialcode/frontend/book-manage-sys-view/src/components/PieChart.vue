<template>
  <div class="line-main" :style="{ backgroundColor: bag }">
    <div>
      <span class="tag" :style="{ color: fontColor }">
        {{ tag }}</span>
    </div>
    <div ref="chart" :style="{ width: width, height: height }"></div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  name: 'PieChart',
  // props 是子元件用來接收「父元件傳進來的資料」的地方
  // 它讓元件之間可以保持資料傳遞的彈性與清晰的資料流（單向資料流）
  // 子元件不要直接修改 props 的值，若需要修改請用 data/computed
  props: {
    types: {
      type: Array,
      default: []
      // default: [] 這樣就算父元件沒給東西，子元件也不會爆掉，而是用預設值安全地執行
    },
    values: {
      type: Array,
      default: []
    },
    width: {
      type: String,
      default: '100%'
    },
    tag: {
      type: String,
      default: '圓餅圖'
    },
    height: {
      type: String,
      default: '243px'
    },
    bag: {
      type: String,
      default: '#fff'
    },
    fontColor: {
      type: String,
      default: '#333'
    },
  },
  data() {
    return {
      chart: null,
    }
  },
  watch: {
    types(v1, v2) {
      this.initChart(); //這確保如果父元件後來才拿到資料（例如 API 請求回來後），圓餅圖還是會自動重新畫一次。如果資料有變：watch 監聽 types，有變就重新畫圖
    }
  },
  mounted() {
    this.initChart(); //初始化圖表 this.$refs.chart 只有在 DOM 已經出現時才有值，所以你必須在 mounted() 裡使用 如果你在 created() 裡用它，會是 undefined，因為畫面還沒掛上去
  },
  methods: {
    initChart() {
      //用 ECharts 初始化圖表，需要傳一個原生 DOM 物件
      //拿到 DOM 元素（圓餅圖的 div）
      //<div ref="chart" :style="{ width: width, height: height }"></div>
      this.chart = echarts.init(this.$refs.chart)
      let option = {
        title: {
          text: '',
          subtext: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          show: false,
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: '70%',
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: false,
                fontSize: '24',
                fontWeight: '600'
              }
            },
            labelLine: {
              show: true
            },
            label: {
              show: true,
              position: 'outer',
              formatter: '{d}%'
            },
            data: this.values.map((value, index) => ({
              name: this.types[index],
              value: value,
              //這一段是把 types 跟 values 組合成 echarts 所需的格式
              //{ name: "存量用户", value: 3},
              //{ name: "公告（篇）", value: 18 },
              //{ name: "書籍（本）", value: 3 }
            })),
            itemStyle: {
              color: function (params) {
                const colorList = [
                  '#409EFF',
                  '#67C23A',
                  '#E6A23C',
                  '#F56C6C',
                  '#909399',
                  '#E4E7ED',
                  '#F2F6FC',
                ];
                return colorList[params.dataIndex % colorList.length];
              }
            }
          }
        ]
      }
      /**
       * option = {} 是 ECharts 的圖表設定（不是 Vue 專用）
         type: 'pie' 就是指定畫「圓餅圖」
         裡面內容是「可調整的模板」：有官方格式，但可以依需求加減設定
         最後透過 this.chart.setOption(option) 把它畫出來
       */
      this.chart.setOption(option)
    }
  },
  /*
  beforeDestroy()	元件即將被銷毀前自動執行的清理鉤子
  用來做什麼？	清除資源、取消監聽、釋放圖表、避免記憶體爆炸
  你需要寫它嗎？	當你用到 外部套件（ECharts、canvas 等） 就非常需要
  */
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  }
}  
</script>

<style scoped lang="scss">
.line-main {
  padding-top: 10px;
  margin-bottom: 5px;
  border-radius: 3px;
  background-color: #000000;

  .tag {
    font-size: 14px;
    text-align: center;
    padding: 15px 6px;
    display: block;
  }

}
</style>