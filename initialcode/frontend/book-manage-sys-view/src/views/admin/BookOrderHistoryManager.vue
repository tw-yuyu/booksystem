<!-- 書籍借閱紀錄管理 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <!-- 上方的搜索框及新增框 -->
            <el-row>
                <<span class="top-bar">借書時間查詢</span>
                    <el-date-picker size="small" style="width: 220px;" v-model="searchTime" type="daterange"
                        range-separator="至" start-placeholder="起始時間" end-placeholder="結束時間" clearable
                        @change="handleFilterClear">
                    </el-date-picker>
                    <el-button size="small" class="customer"
                        style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;"
                        type="primary" @click="handleFilter">立即查詢</el-button>
                    <el-button size="small" class="customer"
                        :style="{ marginLeft: '10px', backgroundColor: selectedRows.length ? '#a7535a' : '#F1F1F1', border: 'none', color: selectedRows.length ? '#FFFFFF' : '#909399' }"
                        type="danger" @click="batchDelete()">批量刪除</el-button>
                    <el-button size="small" class="customer reset"
                        style="background-color: #f1f1f1;border: none;color: #909399;border: 1px solid #f1f1f1;"
                        type="info" @click="resetQueryCondition">條件重置</el-button>
            </el-row>
        </el-row>
        <!-- 搜尋顯示 -->
        <el-row style="margin: 10px 20px;">
            <el-table row-key="id" @selection-change="handleSelectionChange" :data="tableData" style="width: 100%">
                <el-table-column type="selection" width="55"></el-table-column>
                <!-- prop="name" 對應當前行tableData -->
                <!-- 數據來自 tableData 陣列中，每個物件的 name 屬性 -->
                <!-- el-table 會自動遍歷 tableData 陣列 -->
                <el-table-column prop="bookName" width="148" label="書籍名"></el-table-column>
                <el-table-column prop="userName" width="148" label="訂閱者"></el-table-column>
                <el-table-column prop="deadlineNum" width="148" label="借書的數量"></el-table-column>
                <el-table-column prop="isReturn" width="148" label="是否已經歸還">
                    <template slot-scope="scope">
                        <samp>{{ scope.row.isReturn ? '已歸還' : '未歸還' }}</samp>
                    </template>
                </el-table-column>
                <el-table-column prop="returnTime" width="148" label="歸還時間"></el-table-column>
                <el-table-column prop="createTime" width="168" label="借閱時間"></el-table-column>
                <el-table-column label="操作" fixed="right" width="150">
                    <template slot-scope="scope">
                        <div class="operation-buttons">
                            <span class="text-button" @click="handleDelete(scope.row)">刪除</span>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin: 20px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[5, 7]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            data: {}, //空對象 v-model雙向綁定 動態創建的屬性
            currentPage: 1,//當前頁
            cover: null, //照片
            pageSize: 7,//頁面大小  
            totalItems: 0, //總條數 
            dialogOperation: false, // 彈窗開關
            isOperation: false, // 彈窗開關 辨識新增還是修改
            tableData: [],//表格數組
            selectedRows: [],//勾選的數據數組
            bookOrderHistoryQueryDto: {}, // 搜索條件
            searchTime: []//儲存範圍時間 

        };
    },
    created() {
        //初始化會做的事
        this.fetchFreshData();

    },
    methods: {
        // 多選框選中
        handleSelectionChange(selection) {
            this.selectedRows = selection;
        },
        // 批量刪除數據
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`未選中任何數據`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '刪除書籍數據',
                text: `刪除後不可恢復，是否繼續?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/bookOrderHistory/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$swal.fire({
                            title: '刪除提示',
                            text: response.data.msg,
                            icon: 'success',
                            showConfirmButton: false,
                            timer: 2000,
                        });
                        this.fetchFreshData();
                        return;
                    }
                } catch (e) {
                    this.$swal.fire({
                        title: '刪除提示',
                        text: e,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                    console.error(`書籍訊息刪除異常：`, e);
                }
            }
        },
        //重置查詢條件
        resetQueryCondition() {
            this.bookOrderHistoryQueryDto = {};
            this.fetchFreshData();
        },
        //取得最新數據
        async fetchFreshData() {
            try {
                this.tableData = [];
                let startTime = null;
                let endTime = null;
                if (this.searchTime != null && this.searchTime.length === 2) {
                    const start = this.searchTime[0];
                    const end = this.searchTime[1];
                    // 日期格式轉換函式
                    const formatDate = function (date) {
                        const year = date.getFullYear();//拿到年分
                        const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份從0開始，要+1
                        const day = String(date.getDate()).padStart(2, '0');//padStart 補到2位數
                        return `${year}-${month}-${day}`;//字串組合
                    };
                    startTime = `${formatDate(start)}T00:00:00`;
                    endTime = `${formatDate(end)}T23:59:59`;
                    console.log(startTime + " startTime =======================");
                    console.log(endTime + " endTime =======================");
                }
                // 請求參數
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    startTime : startTime,
                    endTime: endTime,
                    ...this.bookOrderHistoryQueryDto //將模糊搜尋裡的字串傳進來並放在data() 在這裡讀取傳給後端 什麼都沒傳就查詢全部mapper  {current: 1, size: 7, startTime: null, endTime: null}
                };
                const response = await this.$axios.post('/bookOrderHistory/query', params); //把params也發送到後端
                const { data } = response;
                this.tableData = data.data; //給data裡的tableData   query後端回傳的資料
                this.totalItems = data.total;//給data裡的totalItems 總條數
            } catch (error) {
                console.error('查詢書籍訊息異常:', error);
            }
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleFilterClear(val) {
            //這樣寫是因為 日期範圍（daterange） 時，清除按鈕（小叉叉）其實不會觸發 @clear 
            //需要使用 @change 判斷被清空才可以  val=v-model="searchTime"
            if (!val || val.length === 0) {
                this.handleFilter();
            }
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
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss">
.list-cover {
    width: 100px;
    height: 120px;
    border-radius: 5px;
}

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

.operation-buttons {
    display: flex;
    gap: 5px;
    /* 按鈕之間的間距 */
    justify-content: center;
    /* 水平置中（或換成 space-between 看需求） */
    align-items: center;
}

.text-button {
    cursor: pointer;
    color: #2b79cb;
    font-size: 14px;
    white-space: nowrap;
    /* 防止文字換行 */
}
</style>