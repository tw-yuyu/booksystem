<!-- 公告 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <!-- 上方的搜索框及新增框 -->
            <el-row>
                <span class="top-bar">標題</span>
                <!-- v-model 雙向綁定 bookQueryDto.name，讓 el-input 和 data() 內的變數保持同步。 -->
                <!-- 模糊搜尋字串傳入進 data()裡的bookQueryDto{}name  -->
                <el-input  @keyup.enter.native="handleFilter" size="small" style="width: 188px;margin-right: 10px;" v-model="noticeQueryDto.name"
                    placeholder="關鍵詞" clearable @clear="handleFilterClear"><!-- 按下搜尋框框裡的XX時 觸發handleFilterClear-->
                </el-input>
                <el-button size="small" class="customer"
                    style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" type="primary"
                    @click="handleFilter">立即查詢</el-button>
            </el-row>
        </el-row>
        <!-- 搜尋顯示 -->
        <el-row style="margin: 10px 20px;">
            <el-table row-key="id" :data="tableData" style="width: 100%">
                <!-- prop="name" 對應當前行tableData -->
                <!-- 數據來自 tableData 陣列中，每個物件的 name 屬性 -->
                <!-- el-table 會自動遍歷 tableData 陣列 -->
                <el-table-column prop="name" label="公告內容"></el-table-column>
                <el-table-column prop="createTime" width="148" label="公告時間"></el-table-column>
                <el-table-column label="操作" fixed="right" width="150">
                    <template slot-scope="scope">
                        <!--  scope.row 代表當前行的數據（對應 tableData 中的每一個元素） 可以靈活運用  scope.row.name就是當前行的name屬性  -->
                        <span class="text-button" @click="noticeSee(scope.row)">查看公告</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin: 20px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[5, 7]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
        <!-- 彈窗面板 -->
        <el-dialog :visible.sync="dialogOperation" width="45%" @close="closeDialog">
            <div slot="title">
                <p class="dialog-title">查看公告</p>
            </div>
            <!-- 顯示彈窗裡的HTML 富文本 -->
            <div style="padding: 0 20px 40px 15px;" v-html="data.content"></div>
        </el-dialog>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            data: {}, //空對象 v-model雙向綁定 動態創建的屬性
            currentPage: 1,//當前頁
            pageSize: 7,//頁面大小  
            totalItems: 0, //總條數 
            dialogOperation: false, // 彈窗開關
            tableData: [],//表格數組
            noticeQueryDto: {}, // 搜尋條件 傳進Query後端
        };
    },
    //初始化會做的事
    created() {
        //取得最新數據
        this.fetchFreshData();
    },
    methods: {

        //按下查看按鈕後彈窗
        noticeSee(notice) {
            this.data = notice;
            this.dialogOperation = true; // 打開對話框（或是顯示編輯界面）

        },
        //重置查詢條件
        resetQueryCondition() {
            this.bookSaveQueryDto = {};
            this.fetchFreshData();
        },
        closeDialog() {
            this.dialogOperation = false;
        },
        //取得最新數據
        async fetchFreshData() {
            try {
                this.tableData = [];//清空tableData
                let startTime = null;
                let endTime = null;
                if (this.searchTime != null && this.searchTime.length === 2) {
                    const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                    startTime = `${startDate.split('T')[0]}T00:00:00`;
                    endTime = `${endDate.split('T')[0]}T23:59:59`;
                }
                // 請求參數
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    ...this.noticeQueryDto, //將模糊搜尋裡的字串傳進來並放在data() 在這裡讀取傳給後端 什麼都沒傳就查詢全部mapper  {current: 1, size: 7, startTime: null, endTime: null}
                    startTime: startTime,//增加查詢開始時間
                    endTime: endTime,//增加查詢結束時間
                };
                const response = await this.$axios.post('/notice/query', params); //把params也發送到後端
                const { data } = response; //const data = response.data;
                this.tableData = data.data; //給data裡的tableData   query後端回傳的資料
                this.totalItems = data.total;//給data裡的totalItems 總條數
            } catch (error) {
                console.error('訂閱通知訊息異常:', error);
            }
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleFilterClear() {
            this.handleFilter();
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchFreshData();
        },
        messagePush(row) {
            this.data = { ...row };
        },
    },
};
</script>
<style scoped lang="scss">
.tag-tip {
    display: inline-block;
    padding: 5px 10px;
    border-radius: 5px;
    background-color: rgb(245, 245, 245);
    color: rgb(104, 118, 130);
}

.input-def {
    height: 40px;
    line-height: 40px;
    outline: none;
    border: none;
    font-size: 20px;
    color: rgb(102, 102, 102);
    font-weight: 900;
    width: 100%;
}

.dialog-footer {
    /* 使按鈕水平置中 */
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 調整按鈕之間的間距 */
.customer {
    margin: 0 8px;
    /* 根據需要調整間距 */
}
</style>