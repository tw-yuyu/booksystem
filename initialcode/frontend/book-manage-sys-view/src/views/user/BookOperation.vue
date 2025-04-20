<!-- 書籍借書 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <!-- 上方的搜索框及新增框 -->
            <el-row>
                <span class="top-bar">書籍名</span>
                <!-- v-model 雙向綁定 bookQueryDto.name，讓 el-input 和 data() 內的變數保持同步。 -->
                <!-- 模糊搜尋字串傳入進 data()裡的bookQueryDto{}name  -->
                <el-input size="small" style="width: 188px;margin-right: 10px;" v-model="bookQueryDto.name"
                    placeholder="書籍名" clearable @clear="handleFilterClear"><!-- 按下搜尋框框裡的XX時 觸發handleFilterClear-->
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
                <el-table-column prop="cover" width="148" label="書籍封面">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" class="list-cover">
                    </template>
                </el-table-column>
                <el-table-column prop="name" width="148" label="書籍名"></el-table-column>
                <el-table-column prop="author" width="148" label="作者"></el-table-column>
                <el-table-column prop="num" width="148" label="館藏數"></el-table-column>
                <el-table-column prop="bookshelfId" width="148" label="書架">
                    <template slot-scope="scope">
                        <samp>{{ bookshelfConfig2(scope.row) }}</samp>
                    </template>
                </el-table-column>
                <el-table-column prop="categoryName" width="148" label="類別"></el-table-column>
                <el-table-column label="操作" fixed="right" width="150">
                    <template slot-scope="scope">
                        <span v-if="scope.row.isPlanBuy">
                            <!--  scope.row 代表當前行的數據（對應 tableData 中的每一個元素） 可以靈活運用  scope.row.name就是當前行的name屬性  -->
                            <span class="text-button" @click="handleEdit(scope.row)">借書</span>
                            <span class="text-button" @click="savebook(scope.row)">收藏這本書</span>
                        </span>
                        <span v-else> 
                            <span class="text-button" @click="rssbook(scope.row)">訂閱</span>
                        </span>
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
                <p class="dialog-title">借書</p>
            </div>
            <!-- 新增表單 -->
            <div style="padding: 0 20px 40px 15px; ">
                <div style="margin-bottom: 10px;">
                    <div class="point">借書數量</div>
                    <el-input-number size="small" v-model="data.deadlineNum" :min="1" :max="10"
                        label="數量"></el-input-number>
                </div>
                <div>
                    <div class="point">歸還日期</div>
                    <el-date-picker size="small" v-model="data.returnTime" type="date" placeholder="選擇日期">
                    </el-date-picker>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="addOperation">確認</el-button>
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
            bookQueryDto: {}, // 搜尋條件 傳進Query後端
        };
    },
    //初始化會做的事
    created() {
        //取得最新數據
        this.fetchFreshData();
    },
    methods: {
        //訂閱書籍
        async rssbook(book) {
            const confirmed = await this.$swalConfirm({
                title: '是否要訂閱這本書',
                text: `訂閱後方便後續操作`,
                icon: 'info',
            });
            if (confirmed) {
                //將欄位裡的BOOKID設置上
                const bookSave = { bookId: book.id }
                try {
                    const response = await this.$axios.post(`/bookRssHistory/save`, bookSave);
                    this.fetchFreshData();
                    this.$notify({
                        title: '訂閱結果',
                        //成功回傳200 失敗回傳400
                        message: response.data.code === 200 ? '訂閱成功' : response.data.msg,
                        type: response.data.code === 200 ? 'success' : 'warning'
                    });

                } catch (e) {
                    console.error(`訂閱失敗`, e);
                    let errorMessage = (e.response && e.response.data && e.response.data.msg) || e.message || "請求失敗，請稍後再試";
                    this.$swal.fire({
                        title: '訂閱結果',
                        text: errorMessage,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                }
            }
        },
        //增加收藏數據
        async savebook(book) {
            const confirmed = await this.$swalConfirm({
                title: '是否要收藏這本書',
                text: `收藏後方便後續操作`,
                icon: 'info',
            });
            if (confirmed) {
                //為什麼是book.id不是id 是因為 savebook(book) 方法帶book進來
                //將欄位裡的BOOKID設置上
                // 一個 bookId 屬性，該屬性的值來自 book.id
                // bookSave = { bookId: 123 };
                const bookSave = { bookId: book.id }
                try {
                    const response = await this.$axios.post(`/bookSave/save`, bookSave);
                    this.fetchFreshData();
                    this.$notify({
                        title: '收藏結果',
                        //成功回傳200 失敗回傳400
                        message: response.data.code === 200 ? '收藏成功' : response.data.msg,
                        type: response.data.code === 200 ? 'success' : 'warning'
                    });

                } catch (e) {
                    console.error(`收藏失敗`, e);
                    let errorMessage = (e.response && e.response.data && e.response.data.msg) || e.message || "請求失敗，請稍後再試";
                    this.$swal.fire({
                        title: '收藏結果',
                        text: errorMessage,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                }
            }
        },
        //將樓層 下拉選單的資料進行拼接 顯示在下拉選單裡 tableData[]
        bookshelfConfig2(item) {
            return item.floor + "-" + item.area + "-" + item.bookshelfName
        },
        //重置查詢條件
        resetQueryCondition() {
            this.bookQueryDto = {};
            this.fetchFreshData();
        },
        // 借書
        async addOperation() {
            try {
                //把欄位裡的ID放進後端物件data的bookId裡 
                // 這裡搜尋顯示的是Id
                this.data.bookId = this.data.id;
                const response = await this.$axios.post('/bookOrderHistory/save', this.data); //提交表單數據data()裡面的data{}
                if (response.data.code === 200) {
                    this.fetchFreshData(); // 重新拉取最新數據，確保畫面更新                     ===============此為新增================
                    this.data = {}; //清空數據
                    this.dialogOperation = false; //關閉彈窗
                    //彈窗通知
                    this.$notify({
                        title: '借書結果',
                        message: '借書成功,請按時歸還',
                        type: 'success'
                    });
                } else {
                    this.$notify({
                        title: '借書結果',
                        message: response.data.msg,
                        type: 'error'
                    });
                }
            } catch (error) {
                this.$notify({
                    title: '借書結果',
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
                this.tableData = [];
                // 請求參數
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    // isPlanBuy: false, //只將isPlanBuy:false的資料拉出來(沒有要購買)
                    ...this.bookQueryDto //將模糊搜尋裡的字串傳進來並放在data() 在這裡讀取傳給後端 什麼都沒傳就查詢全部mapper  {current: 1, size: 7, startTime: null, endTime: null}
                };
                const response = await this.$axios.post('/book/query', params); //把params也發送到後端
                const { data } = response; //const data = response.data;
                this.tableData = data.data; //給data裡的tableData   query後端回傳的資料
                this.totalItems = data.total;//給data裡的totalItems 總條數
            } catch (error) {
                console.error('查詢書籍訊息異常:', error);
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
        //借書按下後
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
</style>