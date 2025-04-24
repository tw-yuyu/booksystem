<!-- 書籍管理 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <!-- 上方的搜索框及新增框 -->
            <el-row>
                <span class="top-bar">書籍名</span>
                <!-- v-model 雙向綁定 bookQueryDto.name，讓 el-input 和 data() 內的變數保持同步。 -->
                <!-- 模糊搜尋字串傳入進 data()裡的bookQueryDto{}name  -->
                <el-input size="small" style="width: 188px;margin-right: 10px;" v-model="bookQueryDto.name"
                    placeholder="書籍名" clearable @clear="handleFilterClear">
                </el-input>
                <el-button size="small" class="customer"
                    style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" type="primary"
                    @click="handleFilter">立即查詢</el-button>
                <el-button size="small"
                    style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="add()">新增書籍</el-button>
                <el-button size="small" class="customer"
                    :style="{ marginLeft: '10px', backgroundColor: selectedRows.length ? '#a7535a' : '#F1F1F1', border: 'none', color: selectedRows.length ? '#FFFFFF' : '#909399' }"
                    type="danger" @click="batchDelete()">批量刪除</el-button>
                <el-button size="small" class="customer reset"
                    style="background-color: #f1f1f1;border: none;color: #909399;border: 1px solid #f1f1f1;" type="info"
                    @click="resetQueryCondition">條件重置</el-button>
            </el-row>
        </el-row>
        <!-- 搜尋顯示 -->
        <el-row style="margin: 10px 20px;">
            <el-table row-key="id" @selection-change="handleSelectionChange" :data="tableData" style="width: 100%">
                <el-table-column type="selection" width="55"></el-table-column>
                <!-- prop="name" 對應當前行tableData -->
                <!-- 數據來自 tableData 陣列中，每個物件的 name 屬性 -->
                <!-- el-table 會自動遍歷 tableData 陣列 -->
                <el-table-column prop="cover" width="148" label="書籍封面">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" class="list-cover">  
                    </template>
                    <!-- /api/book-manage-sys-api/v1.0/file/getFile?fileName=d811c68asdff.jpg -->
                     <!-- 給後端file/getFile的API並回傳圖片的資料流 -->
                </el-table-column>
                <el-table-column prop="name" width="148" label="書籍名"></el-table-column>
                <el-table-column prop="isbn" width="148" label="ISBN"></el-table-column>
                <el-table-column prop="publisher" width="148" label="出版商"></el-table-column>
                <el-table-column prop="author" width="148" label="作者"></el-table-column>
                <el-table-column prop="num" width="148" label="館藏數"></el-table-column>
                <el-table-column prop="bookshelfId" width="148" label="書架">
                    <template slot-scope="scope">
                        <samp>{{ bookshelfConfig2(scope.row) }}</samp>
                    </template>
                </el-table-column>
                <el-table-column prop="categoryName" width="148" label="類別"></el-table-column>
                <el-table-column prop="createTime" width="168" label="入庫時間"></el-table-column>
                <el-table-column label="操作" fixed="right" width="150">
                    <template slot-scope="scope">
                        <div class="operation-buttons">
                            <!--  scope.row 代表當前行的數據（對應 tableData 中的每一個元素） 可以靈活運用  scope.row.cover就是當前行的cover屬性  -->
                            <span v-if="!scope.row.isPlanBuy" class="text-button"@click="postsRssNotification(scope.row)">訂閱上架</span>
                            <span class="text-button" @click="handleEdit(scope.row)">編輯</span>
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
        <!-- 彈窗面板 -->
        <el-dialog :visible.sync="dialogOperation" width="45%" @close="closeDialog">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? '新增書籍' : '修改書籍' }}</p>
            </div>
            <!-- 表單 -->
            <div style="padding:0 20px 40px 15px ; display: flex;">
                <div>
                    <div class="point">書籍封面</div>
                    <el-upload 
                         class="avatar-uploader" 
                         action="https://booksystem-jgi1.onrender.com/api/book-manage-sys-api/v1.0/file/upload" 
                        :show-file-list="false" 
                        :on-success="handleBookCoverSuccess">
                        <img v-if="cover" :src="cover" class="dialog-avatar">   
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <!-- action="/api/book-manage-sys-api/v1.0/file/upload"  -->
                    <!-- v-if="cover"只有當 cover 有值 才會渲染這個 <img> 元素 -->
                    <!-- action指定圖片上傳的 API 路徑 -->
                    <!-- on-success：上傳成功後會呼叫 handleBookCoverSuccess 處理圖片 URL -->
                    <!-- :src="cover"：顯示圖片的封面，會由成功回傳的圖片網址決定 -->
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書架樓層</div>
                        <!-- v-model=data.bookshelfId 就是指這個 data{} bookshelfId 雙向綁定 -->
                        <el-select v-model="data.bookshelfId" placeholder="請選擇">
                            <!-- 在初始化的時候就this.fetchBookShelf(); post('bookshelf/query') 將後端拉出來的值放進bookshelfoptions裡 -->
                            <el-option v-for="(item, index) in bookshelfoptions" :key="index"
                                :label="bookshelfConfig(item)" :value="item.id">
                                <!-- value="item.id" 就是從bookshelfoptions遍歷出來的 item.id 賦值給 data裡的data{}bookshelfId -->
                                <!--bookshelfoptions的資料=item 去遍歷他並放進bookshelfConfig(item)裡進行拼接-->
                                <!--label="bookshelfConfig(item)" 顯示出來 -->
                            </el-option>
                        </el-select>
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書籍類別</div>
                        <el-select v-model="data.categoryId" placeholder="請選擇">
                            <el-option v-for="(item, index) in categoryoptions" :key="index" :label="item.name"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">是否為預售書籍</div>
                        <el-switch v-model="data.isPlanBuy" active-color="#13ce66" inactive-color="rgb(245,245,245)">
                        </el-switch>
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">計畫上架時間(可選)</div>
                        <el-date-picker v-model="data.planBuyTime" type="date" placeholder="計畫上架時間">
                        </el-date-picker>
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">館藏數量</div>
                        <el-input-number size="smail" v-model="data.num" :min="1" :max="100"></el-input-number>
                    </div>
                </div>
                <div style="margin-left: 40px;">
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書籍名</div>
                        <input class="dialog-input" v-model="data.name" />
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">出版社</div>
                        <input class="dialog-input" v-model="data.publisher" />
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書籍的作者</div>
                        <input class="dialog-input" v-model="data.author" />
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">國際標準書號</div>
                        <input class="dialog-input" v-model="data.isbn" />
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書籍簡介</div>
                        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" placeholder="書籍簡介"
                            v-model="data.detail">
                        </el-input>
                    </div>
                </div>

            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation" style="background-color: rgb(43, 121, 203);border: none;"
                    class="customer" type="info" @click="addOperation">新增</el-button>
                <el-button size="small" v-else style="background-color: rgb(43, 121, 203);border: none;"
                    class="customer" type="info" @click="updateOperation">修改</el-button>
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
            cover: null, //照片
            pageSize: 7,//頁面大小  
            totalItems: 0, //總條數 
            dialogOperation: false, // 彈窗開關
            isOperation: false, // 彈窗開關 辨識新增還是修改 false是新增彈窗 tuer是修改彈窗
            tableData: [],//表格數組
            selectedRows: [],//勾選的數據數組
            bookQueryDto: {}, // 搜索條件
            categoryoptions: [],//下拉選框
            bookshelfoptions: []//下拉選框

        };
    },
    created() {
        //初始化會做的事
        this.fetchFreshData();
        this.fetchCategroy();
        this.fetchBookShelf();
    },
    methods: {

        //照片上傳
        handleBookCoverSuccess(res, file) {
            if (res.code !== 200) {
                this.$message.error(`書籍封面上傳異常`);
                return;
            }
            this.$message.success(`書籍封面上傳成功`);
            console.log("Uploaded cover URL:", res.data);
            this.cover = res.data;//賦值給data()裡的coverd讓前端能讀取照片 /api/book-manage-sys-api/v1.0/file/getFile?fileName=55cee0bpic_4.jpg
            this.data.cover = res.data; //賦值給data()裡的data{}         /api/book-manage-sys-api/v1.0/file/getFile?fileName=55cee0bpic_4.jpg
        },
        //下拉選單 重新整理就先拉出Categroy表
        fetchCategroy() {
            this.$axios.post('category/query', {}).then(res => {
                if (res.data.code === 200) {
                    //將搜索到的資料放進options裡
                    this.categoryoptions = res.data.data
                }
            })
        },
        //下拉選單 重新整理就先拉出BookShelf表
        fetchBookShelf() {
            this.$axios.post('bookshelf/query', {}).then(res => {
                //將搜索到的資料放進options裡
                if (res.data.code === 200) {
                    this.bookshelfoptions = res.data.data
                }
            })
        },
        //將樓層 下拉選單的資料進行拼接 顯示在下拉選單裡 bookshelfoptions[]
        bookshelfConfig(item) {
            return item.floor + "-" + item.area + "-" + item.name
        },
        //將樓層 下拉選單的資料進行拼接 顯示在下拉選單裡 tableData[]
        bookshelfConfig2(item) {
            return item.floor + "-" + item.area + "-" + item.bookshelfName
        },
        // 多選框選中
        handleSelectionChange(selection) {
            this.selectedRows = selection;
        },

        // 跟有訂閱此書的人發上線消息
        async postsRssNotification(book) {
            const confirmed = await this.$swalConfirm({
                title: '書籍已上線?',
                text: `是否繼續?`,
                icon: 'info',
            });
            if (confirmed) {
                //將欄位裡的BOOKID設置上
                //為什麼是book.id不是id 是因為 savebook(book) 方法帶book進來
                // 一個 bookId 屬性，該屬性的值來自 book.id
                // bookSave = { bookId: 123 };
                // const BookRssHistory = { id : book.id }
                try {
                    const response = await this.$axios.get(`/rssNotification/postsRssNotification/${book.id}`);
                    this.fetchFreshData();
                    this.$notify({
                        title: '通知成功',
                        //成功回傳200 失敗回傳400
                        message: response.data.code === 200 ? '通知成功' : response.data.msg,
                        type: response.data.code === 200 ? 'success' : 'warning'
                    });

                } catch (e) {
                    console.error(`通知失敗`, e);
                    let errorMessage = (e.response && e.response.data && e.response.data.msg) || e.message || "請求失敗，請稍後再試";
                    this.$swal.fire({
                        title: '通知結果',
                        text: errorMessage,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                }
            }
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
                    const response = await this.$axios.post(`/book/batchDelete`, ids);
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
            this.bookQueryDto = {};
            this.fetchFreshData();
        },
        // 修改信息
        async updateOperation() {

            try {
                this.data.cover = this.cover; // 確保圖片 URL 也被更新
                const response = await this.$axios.put('/book/update', this.data);//提交表單數據data()裡面的data{}
                this.$swal.fire({
                    title: '書籍訊息修改',
                    text: response.data.msg,
                    icon: response.data.code === 200 ? 'success' : 'error',
                    showConfirmButton: false,
                    timer: 1000,
                });
                if (response.data.code === 200) {
                    this.closeDialog();
                    this.fetchFreshData();
                    this.clearFormData();
                }
            } catch (error) {
                console.error('提交表單時出錯:', error);
                this.$message.error('提交失敗，請稍後在試！');
            }
        },
        // 信息新增
        async addOperation() {

            try {
                const response = await this.$axios.post('/book/save', this.data); //提交表單數據data()裡面的data{}
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.closeDialog(); //關閉當前的對話框 
                    this.fetchFreshData(); // 重新拉取最新數據，確保畫面更新                     
                    this.clearFormData();//清空表單數據，避免舊數據影響下一次輸入
                }
            } catch (error) {
                console.error('提交表單時出錯:', error);
                this.$message.error('提交失敗，請稍後在試！');
            }
        },
        //當沒有完成表單修改 離開彈窗時也會關閉對話框並清空
        closeDialog() {
            this.dialogOperation = false; // 關閉對話框
            this.clearFormData(); // 確保資料清空
        },
        // 關閉彈窗清除表單資訊 順便將isOperation設定為新增彈窗                   
        clearFormData() {
            this.data = {};     //將data{}為空 清除
            this.isOperation = false; // 重置為新增彈窗
            this.cover = null; //將cover設為原來狀態NULL 清除
        },
        //取得最新數據
        async fetchFreshData() {
            try {
                this.tableData = [];
                // 請求參數
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    ...this.bookQueryDto //將模糊搜尋裡的字串傳進來並放在data() 在這裡讀取傳給後端 什麼都沒傳就查詢全部mapper  {current: 1, size: 7, startTime: null, endTime: null}
                };
                const response = await this.$axios.post('/book/query', params); //把params也發送到後端
                const { data } = response;
                this.tableData = data.data; //給data裡的tableData   query後端回傳的資料
                this.totalItems = data.total;//給data裡的totalItems 總條數
                console.log("後端回傳的 cover：", data.data.map(item => item.cover));
            } catch (error) {
                console.error('查詢書籍訊息異常:', error);
            }
        },
        // 按下新增後彈窗
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
        //修改編輯按下後跳出的表單 帶有那一行的tableData資料
        handleEdit(row) {
            this.isOperation = true; // 設定操作模式為編輯
            this.dialogOperation = true; // 打開對話框（或是顯示編輯界面）
            this.data = { ...row } // 將傳入的 `row` 物件的內容複製一份並賦值給 `data`
            this.cover = row.cover; //將row裡的img src 傳入data()cover 可以更新讀取圖片 就是tableData裡的cover 
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