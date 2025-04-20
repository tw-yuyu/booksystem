<!-- 書籍類別管理 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <el-row>
                <span class="top-bar">類別名</span>
                <el-input size="small" style="width: 188px;margin-right: 10px;" v-model="categoryQueryDto.name" placeholder="書籍類別名" clearable
                    @clear="handleFilterClear">
                </el-input>
                <el-button size="small" class="customer" style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;"
                    type="primary" @click="handleFilter">立即查詢</el-button>
                <el-button size="small" style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="add()">新增書籍類別</el-button>
                <el-button size="small" class="customer"
                    :style="{ marginLeft: '10px', backgroundColor: selectedRows.length ? '#a7535a' : '#F1F1F1', border: 'none', color: selectedRows.length ? '#FFFFFF' : '#909399' }"
                    type="danger" @click="batchDelete()">批量刪除</el-button>
                <el-button size="small" class="customer reset"
                    style="background-color: #f1f1f1;border: none;color: #909399;border: 1px solid #f1f1f1;" type="info"
                    @click="resetQueryCondition">條件重置</el-button>
            </el-row>
        </el-row>
        <el-row style="margin: 10px 20px;">
            <el-table row-key="id" @selection-change="handleSelectionChange" :data="tableData" style="width: 100%">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="name" width="148" label="書籍類別名"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
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
        <el-dialog :visible.sync="dialogOperation" width="25%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? '新增類別' : '修改類別' }}</p>
            </div>
            <div style="padding:0 20px 40px 15px ;">
                    <input class="dialog-input" v-model="data.name" placeholder="書籍類別名" />
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
            data: {}, //空對象
            currentPage: 1,//當前頁
            pageSize: 7,//頁面大小  
            totalItems: 0, //總條數 
            dialogOperation: false, // 彈窗開關
            isOperation: false, // 彈窗開關 辨識新增還是修改
            tableData: [],//表格數組
            selectedRows: [],//勾選的數據數組
            categoryQueryDto: {}, // 搜索條件
            messsageContent: ''
        };
    },
    created() {
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
                title: '刪除書籍類別數據',
                text: `刪除後不可恢復，是否繼續?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/category/batchDelete`, ids);
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
                    console.error(`書籍類別訊息刪除異常：`, e);
                }
            }
        },
        resetQueryCondition() {
            this.categoryQueryDto = {};
            this.fetchFreshData();
        },
        // 修改信息
        async updateOperation() {
            // if (this.userPwd !== '') {
            //     const pwd = this.$md5(this.$md5(this.userPwd));
            //     this.data.userPwd = pwd;
            // } else {
            //     this.data.userPwd = null;
            // }
            try {
                const response = await this.$axios.put('/category/update', this.data);
                this.$swal.fire({
                    title: '書籍類別訊息修改',
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
                const response = await this.$axios.post('/category/save', this.data);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
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
        closeDialog() {
            this.dialogOperation = false;
        },
        clearFormData() {
            this.data = {};
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
                    ...this.categoryQueryDto
                };
                const response = await this.$axios.post('/category/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查詢書籍類別訊息異常:', error);
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
        handleEdit(row) {
            this.dialogOperation = true;
            this.isOperation = true;
            row.userPwd = null;
            this.data = { ...row }
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