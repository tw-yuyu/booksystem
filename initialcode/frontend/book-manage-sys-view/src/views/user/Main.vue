<!-- 留言板 -->
<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <el-row>
                <el-button size="small" class="customer"
                    style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" type="primary"
                    @click="toFeedback">我要反饋</el-button>
                <span style=" float: right;">
                    <el-input size="small" style="width: 188px;margin-right: 10px;" v-model="content" placeholder="問題"
                        clearable @clear="handleFilterClear">
                    </el-input>
                    <el-button size="small" class="customer"
                        style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;"
                        type="primary" @click="handleFilter">立即查詢</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 10px 20px;">
            <div class="item-proposal" v-for="(item, index) in tableData" :key="index">
                <div style="margin-bottom: 6px">
                    <span class="item-flag">
                        {{ item.rePlyContent !== null ? '已回覆' : '未回覆' }}
                    </span>
                    <span>{{ item.userName }}反饋內容:</span>
                    <span>{{ item.content }}</span>
                </div>
                <div v-if="item.rePlyContent !== null" style="margin-bottom: 6px">
                    管理員回覆內容:{{ item.rePlyContent }}
                </div>
                <div style="margin-bottom: 6px">
                    <span>反饋時間:{{ item.createTime }}</span>
                    <!-- 如果管理者沒有回覆就不顯示回答時間 -->
                    <span v-if="item.rePlyContent !== null" style="margin-left: 10px">回答時間:{{ item.rePlyTime }}</span>
                </div>
            </div>
            <el-pagination style="margin: 20px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[5, 7]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
        <!-- 彈窗面板 -->
        <el-dialog :visible.sync="dialogOperation" width="25%">
            <div slot="title">
                <p class="dialog-title">反饋的問題</p>
            </div>
            <div style="padding:0 20px 40px 15px ;">
                <div style="margin-bottom: 10px">
                    <el-input type="textarea" :autosize="{ minRows: 6, maxRows: 6 }" placeholder="請輸入內容"
                        v-model="data.content">
                    </el-input>
                </div>
                <div class="point">公開權限</div>
                <el-switch v-model="data.isPublish" active-text="公開" inactive-text="不公開"></el-switch>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="addOperation">確定</el-button>
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
            content: null,

        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        toFeedback() {
            this.dialogOperation = true;
        }
        ,

        // 多選框選中
        handleSelectionChange(selection) {
            this.selectedRows = selection;
        },

        resetQueryCondition() {
            this.readerproposalQueryDto = {};
            this.fetchFreshData();
        },

        // 新增
        async addOperation() {

            try {
                const response = await this.$axios.post('/readerproposal/save', this.data);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.$notify({
                        title: '讀者反饋',
                        message: '你的反饋我們收到了，請關注管理員的回覆',
                        type: 'success'
                    });
                    this.dialogOperation = false; //關閉彈窗
                    this.data = {}; //清空數據避免引響下一次
                    this.fetchFreshData();//從新拉取新資料
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
                // 請求參數
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    //只搜尋isPublish有true
                    isPublish: true,
                    content: this.content,
                };
                const response = await this.$axios.post('/readerproposal/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('反饋訊息異常:', error);
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
.item-proposal {
    padding-block: 20px;
    font-size: 14px;
    color: rgb(121, 121, 121);
    border-bottom: 1px solid rgb(245, 245, 245);
}

.item-flag {
    display: inline-block;
    padding: 3px 8px;
    border-radius: 2px;
    background-color: rgb(220, 235, 248);
    color: rgb(45, 28, 199);
    margin-right: 10px;
    font-size: 12px;
}
</style>