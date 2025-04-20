<template>
    <div style="padding: 0 10px;overflow-y: hidden;overflow-x: hidden;">
        <el-row>
            <el-col :span="8">
                <div style="padding: 10px;box-sizing: border-box;">
                    <PieChart fontColor="#000" 
                              bag="rgb(236, 245, 255)" 
                              tag="數據基礎" 
                              :values="pieValues"
                              :types="pieTypes" />
                <!--這段是 Vue 的 props 傳遞，父元件將 pieValues 和 pieTypes 傳給了子元件 PieChart.vue，子元件就可以透過 props 使用這些資料。 -->
                </div>
                <div style="padding: 10px 20px;box-sizing: border-box;">
                    <h3>最新公告</h3>
                    <div style="background-color: rgb(236, 245, 255);border-radius: 5px;padding: 5px 10px;">
                        <div style="margin-bottom: 20px;margin-top: 5px;" v-for="(notice, index) in noticeList" :key="index">
                            <div style="margin: 5px 0;cursor: pointer;">
                                <span style="font-size: 14px;">{{ notice.name }}</span>
                            </div>
                            <div>
                                <span style="font-size: 14px;">時間：{{ notice.createTime }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="16">
                <!-- LineChart 讀取專案中 @/components/LineChart.vue -->
                <div style="padding: 8px;box-sizing: border-box;"> 
                    <LineChart height="310px" 
                    tag="用戶數" 
                    @on-selected="userDatesSelected" 
                    :values="userValues"
                    :date="userDates" />
                </div>
                <div style="padding: 8px;box-sizing: border-box;">
                    <LineChart height="310px"
                     tag="書籍上架情況" 
                     @on-selected="modelDatesSelected" 
                     :values="modelValues"
                     :date="modelDates" />
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import LineChart from "@/components/LineChart"
//從 src/components/LineChart.vue（或 .js）這個路徑載入一個自定義元件。
import PieChart from "@/components/PieChart"
export default {
    components: { LineChart, PieChart },
    data() {
        return {
            userValues: [], // 用戶折線圖的 Y 軸資料（例如每天的用戶數）
            userDates: [],  // 用戶折線圖的 X 軸資料（對應的日期）
            modelDates: [],  // 書籍上架情況折線圖的 X 軸資料（對應的日期）
            modelValues: [], // 書籍上架情況折線圖的 Y 軸資料（每天的收錄問卷數）
            pieValues: [],  // 圓餅圖的數值陣列（每一個分類的數量，例如使用者數、公告數等）         {3,18,3}
            pieTypes: [],  // 圓餅圖的分類名稱（每個扇形的名稱，例如「使用者」「公告」「書籍」）     {存量用戶,公告（篇）,書籍（本）}
            noticeList: [] // 最新公告列表（用來在右邊公告區塊 v-for 顯示）
        }
    },
    created() {
        this.userDatesSelected(365);  // 查詢用戶數折線圖資料
        this.modelDatesSelected(365); // 查詢書籍上架情況折線圖資料
        this.loadPieCharts();         // 這裡就是拿圓餅圖資料！       
        this.loadMessage();           // 查詢公告  
    },
    methods: {
        // 加載公告數據
        loadMessage() {
            const messageQueryDto = {
                current: 1,
                size: 4
            }
            this.$axios.post(`/notice/query`, messageQueryDto).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.noticeList = data.data;
                }
            })
        },
        //基礎數據(圓餅圖)
        loadPieCharts() {
            // .then() 就是 等資料來了再執行的函數
            this.$axios.get(`/views/staticControls`).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.pieValues = data.data.map(entity => entity.count);
                    this.pieTypes = data.data.map(entity => entity.name);
                }
            })
        },
        //書籍上架情況
        modelDatesSelected(time) {
            this.$axios.get(`/book/daysQuery/${time}`).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    //.map() 是 JavaScript 陣列的方法，它會「對陣列的每一個元素進行操作，並回傳一個新的陣列」。
                    this.modelValues = data.data.map(entity => entity.count);
                    this.modelDates = data.data.map(entity => entity.name);
                }
            })
        },
        //用戶數
        userDatesSelected(time) {
            this.$axios.get(`/user/daysQuery/${time}`).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.userValues = data.data.map(entity => entity.count);
                    this.userDates = data.data.map(entity => entity.name);
                }
            })
        },
    },
};
</script>
<style scoped lang="scss">
.status-success {
    display: inline-block;
    padding: 1px 5px;
    border-radius: 2px;
    background-color: rgb(201, 237, 249);
    color: rgb(111, 106, 196);
    font-size: 12px;
}

.status-error {
    display: inline-block;
    padding: 1px 5px;
    border-radius: 2px;
    background-color: rgb(233, 226, 134);
    color: rgb(131, 138, 142);
    color: rgb(111, 106, 196);
    font-size: 12px;
}
</style>