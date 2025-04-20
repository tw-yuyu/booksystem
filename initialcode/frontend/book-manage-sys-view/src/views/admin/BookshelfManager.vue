<!-- 書架管理 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <!-- 上方的搜索框及新增框 -->
            <el-row>
                <span class="top-bar">書架名</span>
                <!-- 模糊搜尋字串傳入進 data()bookshelfQueryDto{}floor  -->
                <el-input size="small" style="width: 188px;margin-right: 10px;" v-model="bookshelfQueryDto.floor"
                    placeholder="書架名" clearable @clear="handleFilterClear">
                </el-input>
                <el-button size="small" class="customer"
                    style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" type="primary"
                    @click="handleFilter">立即查詢</el-button>
                <el-button size="small"
                    style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="add()">新增書架</el-button>
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
                <!-- prop="name" 對應tableData -->
                <el-table-column prop="area" width="148" label="書架所在區域"></el-table-column>
                <el-table-column prop="floor" width="148" label="書架所在樓層"></el-table-column>
                <el-table-column prop="name" width="148" label="書架名"></el-table-column>
                <el-table-column prop="frame" width="148" label="書架框架"></el-table-column>
                <el-table-column label="操作" >
                    <template slot-scope="scope">
                        <!--  每一行數據的所有屬性（例如書名、作者、ISBN 等）都會保存在 row 這個對象中 將 scope.row 傳入 handleEdit 賦值 -->
                        <span class="text-button" @click="handleEdit(scope.row)">編輯</span>
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
        <el-dialog :visible.sync="dialogOperation" width="25%" @close="closeDialog">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? '新增書架' : '修改書架' }}</p>
            </div>
            <!-- 表單 -->
            <div style="padding:0 20px 40px 15px">
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書架所在樓層</div>
                        <input class="dialog-input" v-model="data.floor" placeholder="樓層"/>
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書架所在區域</div>
                        <input class="dialog-input" v-model="data.area" placeholder="區域"/>
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書架名</div>
                        <input class="dialog-input" v-model="data.name" placeholder="名稱"/>
                    </div>
                    <div style="margin-bottom: 5px ;">
                        <div class="point">書架框架</div>
                        <input class="dialog-input" v-model="data.frame" placeholder="框架"/>
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
            data: {}, //空對象 v-model賦值
            currentPage: 1,//當前頁
            cover: null, //照片
            pageSize: 7,//頁面大小  
            totalItems: 0, //總條數 
            dialogOperation: false, // 彈窗開關
            isOperation: false, // 彈窗開關 辨識新增還是修改
            tableData: [],//表格數組
            selectedRows: [],//勾選的數據數組
            bookshelfQueryDto: {}, // 搜索條件
            options: [],//下拉選框

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
                title: '刪除書架數據',
                text: `刪除後不可恢復，是否繼續?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/bookshelf/batchDelete`, ids);
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
                    console.error(`書架訊息刪除異常：`, e);
                }
            }
        },
        resetQueryCondition() {
            this.bookshelfQueryDto = {};
            this.fetchFreshData();
        },
        // 修改信息
        async updateOperation() {

            try {
                this.data.cover = this.cover; // 確保圖片 URL 也被更新
                const response = await this.$axios.put('/bookshelf/update', this.data); //提交表單數據data()裡面的data{}
                this.$swal.fire({
                    title: '書架訊息修改',
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
                const response = await this.$axios.post('/bookshelf/save', this.data); //提交表單數據data()裡面的data{}
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.closeDialog(); //關閉當前的對話框 
                    this.fetchFreshData(); // 重新拉取最新數據，確保畫面更新                     ===============此為新增================
                    this.clearFormData();//清空表單數據，避免舊數據影響下一次輸入
                }
            } catch (error) {
                console.error('提交表單時出錯:', error);
                this.$message.error('提交失敗，請稍後在試！');
            }
        },
        closeDialog() {
            this.dialogOperation = false;
        },
        //當沒有完成表單修改 離開彈窗時也會關閉對話框並清空
        closeDialog() {
            this.dialogOperation = false; // 關閉對話框
            this.clearFormData(); // 確保資料清空
        },
        // 關閉彈窗清除表單資訊 順便將isOperation設定為新增彈窗                   ===============此為新增================
        clearFormData() {
            this.data = {};     //將data{}為空 清除
            this.isOperation = false; // 重置為新增彈窗
            this.cover = null; //將cover設為原來狀態NULL 清除
        },
        async fetchFreshData() {
            try {
                this.tableData = [];
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
                    startTime: startTime,
                    endTime: endTime,
                    ...this.bookshelfQueryDto //將模糊搜尋裡的字串傳進來並放在data() 在這裡讀取傳給後端 什麼都沒傳就查詢全部mapper 
                };
                const response = await this.$axios.post('/bookshelf/query', params); //把params也發送到後端
                const { data } = response;
                this.tableData = data.data; //給data裡的tableData   query後端回傳的資料
                this.totalItems = data.total;//給data裡的totalItems 總條數
            } catch (error) {
                console.error('查詢書架訊息異常:', error);
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
        //修改編輯按下後
        handleEdit(row) {
            this.dialogOperation = true; // 打開對話框（或是顯示編輯界面）
            this.isOperation = true; // 設定操作模式為編輯
            this.data = { ...row } // 將傳入的 `row` 物件的內容複製一份並賦值給 `data`
            this.cover = row.cover; //將row裡的img src 傳入data()cover 可以更新讀取圖片
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