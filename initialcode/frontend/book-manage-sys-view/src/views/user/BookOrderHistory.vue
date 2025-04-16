<!-- 書籍借閱歷史 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <!-- 上方的搜索框及新增框 -->
            <el-row>
                <<span class="top-bar">借書時間查詢</span>
                    <el-date-picker size="small" style="width: 220px;" v-model="searchTime" type="daterange"
                        range-separator="至" start-placeholder="起始時間" end-placeholder="結束時間">
                    </el-date-picker>
                    <el-button size="small" class="customer"
                        style="margin-left:10px; background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;"
                        type="primary" @click="handleFilter">立即查詢</el-button>
            </el-row>
        </el-row>
        <!-- 搜尋顯示 -->
        <el-row style="margin: 10px 20px;">
            <el-table row-key="id" :data="tableData" style="width: 100%">
                <!-- prop="name" 對應當前行tableData -->
                <!-- 數據來自 tableData 陣列中，每個物件的 name 屬性 --> 
                <!-- el-table 會自動遍歷 tableData 陣列 -->
                <el-table-column prop="bookName" width="148" label="書籍名"></el-table-column>
                <el-table-column prop="deadlineNum" width="138" label="借閱數"></el-table-column>
                <el-table-column prop="isReturn" width="148" label="是否已歸還">
                    <!-- 如果是ture就顯示已歸還 false就顯示未歸還 -->
                    <template slot-scope="scope">
                        <samp>{{ scope.row.isReturn ? '已歸還' : '未歸還' }}</samp>
                    </template>
                </el-table-column>
                <el-table-column prop="returnTime" width="128" label="歸還時間"></el-table-column>
                <el-table-column prop="createTime" width="150" label="借書時間"></el-table-column>
                <el-table-column label="操作" fixed="right" width="150">
                    <template slot-scope="scope">
                        <!--  scope.row 代表當前行的數據（對應 tableData 中的每一個元素） 可以靈活運用  scope.row.name就是當前行的name屬性  -->
                        <span class="text-button" @click="handleEdit(scope.row)">還書</span>
                        <span class="text-button" @click="handleDelete(scope.row)">刪除</span>
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
                <p class="dialog-title">還書</p>
            </div>
            <!-- 新增表單 -->
            <div style="padding: 0 20px 40px 15px; ">
                <div style="margin-bottom: 10px;">
                    <div class="point">歸還提醒:</div>
                    <div class="point">歸還之後,可繼續借書</div>
                </div>

            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="returnOperation">確認</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogOperation = false">取消</el-button>
            </span>
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
            isOperation: false, // 彈窗開關 辨識新增還是修改
            tableData: [],//表格數組
            selectedRows: [],//勾選的數據數組
            bookOrderHistoryQueryDto: {}, // 搜尋條件 傳進Query後端
            searchTime: []//儲存範圍時間 

        };
    },
    //初始化會做的事
    created() {
        //取得最新數據
        this.fetchFreshData();
    },
    methods: {
        //重置查詢條件
        resetQueryCondition() {
            this.bookOrderHistoryQueryDto = {};
            this.fetchFreshData();
        },
        // 還書
        async returnOperation() {
            try {
                //發之前組裝數據
                const bookOrderHistory = {
                    id:this.data.id,
                    isReturn:true
                }
                // console.log(id)
                const response = await this.$axios.put('/bookOrderHistory/update', bookOrderHistory); //提交表單數據data()裡面的data{}
                if (response.data.code === 200) {
                    this.fetchFreshData(); // 重新拉取最新數據，確保畫面更新                     ===============此為新增================
                    this.data = {}; //清空數據
                    this.dialogOperation = false; //關閉彈窗
                    //彈窗通知
                    this.$notify({
                        title: '還書結果',
                        message: '還書成功',
                        type: 'success'
                    });
                } else {
                    this.$notify({
                        title: '還書結果',
                        message: response.data.msg,
                        type: 'error'
                    });
                }
            } catch (error) {
                this.$notify({
                    title: '還書結果',
                    message: error,
                    type: 'error'
                });
            }
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
                    ...this.bookOrderHistoryQueryDto, //將模糊搜尋裡的字串傳進來並放在data() 在這裡讀取傳給後端 什麼都沒傳就查詢全部mapper  {current: 1, size: 7, startTime: null, endTime: null}
                    startTime: startTime,//增加查詢開始時間
                    endTime: endTime,//增加查詢結束時間
                };
                const response = await this.$axios.post('/bookOrderHistory/queryUser', params); //把params也發送到後端
                const { data } = response; //const data = response.data;
                this.tableData = data.data; //給data裡的tableData   query後端回傳的資料
                this.totalItems = data.total;//給data裡的totalItems 總條數
            } catch (error) {
                console.error('借閱歷史訊息異常:', error);
            }
        },
        // 批量刪除數據
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`未選中任何數據`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '刪除預約數據',
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
                    console.error(`預約訊息刪除異常：`, e);
                }
            }
        },
        add() {
            this.dialogOperation = true;
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
        //還書按下後
        handleEdit(row) {
            this.dialogOperation = true; // 打開對話框（或是顯示編輯界面）
            this.data = { ...row } // 將傳入的 `row` 物件的內容複製一份並賦值給 `data`
        },
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
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