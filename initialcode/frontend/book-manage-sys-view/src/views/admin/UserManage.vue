<template>
    <el-row style="background-color: #FFFFFF;padding: 20px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 10px;">
            <el-row>
                <span class="top-bar">用戶名</span>
                <el-input size="small" style="width: 188px;margin-right: 10px;" v-model="userQueryDto.userName" placeholder="用戶名" clearable
                    @clear="handleFilterClear">
                </el-input>
                <span class="top-bar">註冊時間</span>
                <el-date-picker size="small" style="width: 220px;" v-model="searchTime"
                    type="daterange" range-separator="至" start-placeholder="起始時間" end-placeholder="結束時間">
                </el-date-picker>
                <el-button size="small" class="customer" style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;"
                    type="primary" @click="handleFilter">立即查詢</el-button>
                <el-button size="small" style="background-color: rgb(235, 237, 245);color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="add()">新增用戶</el-button>
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
                <el-table-column prop="userAvatar" width="68" label="頭像">
                    <template slot-scope="scope">
                        <el-avatar :size="30" :src="scope.row.userAvatar" style="margin-top: 10px;"></el-avatar>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" width="148" label="名稱"></el-table-column>
                <el-table-column prop="userAccount" width="128" label="帳號"></el-table-column>
                <el-table-column prop="userEmail" width="168" label="用戶郵箱"></el-table-column>
                <el-table-column prop="userRole" width="68" label="角色">
                    <template slot-scope="scope">
                        <span>{{ scope.row.userRole === 1 ? '管理員' : '用戶' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="isLogin" width="108" label="封號">
                    <template slot-scope="scope">
                        <el-switch @change="handleSwitchChange(scope.row.id, scope.row.isLogin, true)"
                            style="user-select: none;" v-model="scope.row.isLogin" active-color="#13ce66"
                            inactive-color="rgb(226, 226, 226)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column :sortable="true" prop="createTime" width="168" label="註冊於"></el-table-column>
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
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogUserOperaion" width="28%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? '新增新用戶' : '編輯用戶信息' }}</p>
            </div>
            <div style="padding:0 20px;">
                <el-row>
                    <el-upload class="avatar-uploader" action="/api/book-manage-sys-api/v1.0/file/upload"
                        :show-file-list="false" :on-success="handleAvatarSuccess">
                        <img v-if="data.userAvatar" :src="data.userAvatar" class="dialog-avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-row>
                <el-row>
                    <span class="dialog-hover">用戶名</span>
                    <input class="dialog-input" v-model="data.userName" placeholder="用戶名" />
                    <span class="dialog-hover">帳號</span>
                    <input class="dialog-input" v-model="data.userAccount" placeholder="帳號" />
                    <span class="dialog-hover">郵箱</span>
                    <input class="dialog-input" v-model="data.userEmail" placeholder="郵箱" />
                    <span class="dialog-hover">密碼</span>
                    <input class="dialog-input" v-model="userPwd" type="password" placeholder="密碼" />
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation" style="background-color: rgb(43, 121, 203);border: none;"
                    class="customer" type="info" @click="addOperation">新增</el-button>
                <el-button size="small" v-else style="background-color: rgb(43, 121, 203);border: none;"
                    class="customer" type="info" @click="updateOperation">修改</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogUserOperaion = false">取消</el-button>
            </span>
        </el-dialog>
        <!-- 消息推送 -->
        <el-dialog :show-title="false" :show-close="false" :visible.sync="dialogMessageOperation" width="24%">
            <p style="padding: 20px 0  0 20px;">消息推送</p>
            <div style="padding:0 20px;">
                <el-row>
                    <span class="dialog-hover">消息內容</span>
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" placeholder="消息內容"
                        v-model="data.content">
                    </el-input>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="messagePushOperation">確定推送</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogMessageOperation = false">取消</el-button>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            userPwd: '',
            data: { userAvatar: '' },
            filterText: '',
            currentPage: 1,
            pageSize: 7,
            totalItems: 0,
            dialogMessageOperation: false,
            dialogUserOperaion: false, // 開關
            isOperation: false, // 開關-標識新增或修改
            tableData: [],
            searchTime: [],
            selectedRows: [],
            status: null,
            userQueryDto: {}, // 搜索條件
            messsageContent: ''
        };
    },
    watch: {
        dialogUserOperaion(v1, v2) {
            if (!v1) {
                this.isOperation = !this.isOperation;
            }
            if (!v1 && v2) {
                this.data = {};
            }
        },
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        // 處理消息的推送
        messagePushOperation() {
            const messages = []
            const message = {
                receiverId: this.data.id,
                content: this.data.content
            }
            messages.push(message);
            this.$axios.post('/message/systemInfoSave', messages).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.$swal.fire({
                        title: '消息推送',
                        text: '推送成功',
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 1000,
                    });
                    this.dialogMessageOperation = false;
                    this.data = {};
                }
            })
        },
        handleAvatarSuccess(res, file) {
            if (res.code !== 200) {
                this.$message.error(`用戶頭像上傳異常`);
                return;
            }
            this.$message.success(`用戶頭像上傳成功`);
            this.data.userAvatar = res.data;
            console.log(this.data);
        },
        switchChange() {
            this.fetchFreshData();
        },
        async handleSwitchChange(id, status, operation) {
            try {
                let param = { id: id }
                // 登錄狀態
                if (operation) {
                    param.isLogin = status;
                } else { // 評論狀態
                    param.isWord = status;
                }
                const response = await this.$axios.put(`/user/backUpdate`, param);
                if (response.data.code === 200) {
                    this.$swal.fire({
                        title: operation ? '登錄狀態' : '評論狀態',
                        text: operation ? '登錄狀態操作成功' : '評論狀態操作成功',
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 1000,
                    });
                }
            } catch (e) {
                console.error(`更新用戶狀態異常：${e}`);
            }
        },
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
                title: '刪除用戶數據',
                text: `刪除後不可恢復，是否繼續？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/user/batchDelete`, ids);
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
                        title: '錯誤提示',
                        text: e,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                    console.error(`用戶信息刪除異常：`, e);
                }
            }
        },
        resetQueryCondition() {
            this.userQueryDto = {};
            this.searchTime = [];
            this.fetchFreshData();
        },
        // 修改信息
        async updateOperation() {
            if (this.userPwd !== '') {
                const pwd = this.$md5(this.$md5(this.userPwd));
                this.data.userPwd = pwd;
            } else {
                this.data.userPwd = null;
            }
            try {
                const response = await this.$axios.put('/user/backUpdate', this.data);
                this.$swal.fire({
                    title: '用戶信息修改',
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
                this.$message.error('提交失敗，請稍後再試！');
            }
        },
        // 信息新增
        async addOperation() {
            if (this.userPwd !== '') {
                this.data.userPwd = this.$md5(this.$md5(this.userPwd));
            } else {
                this.data.userPwd = null;
            }
            try {
                const response = await this.$axios.post('/user/insert', this.data);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.closeDialog();
                    this.fetchFreshData();
                    this.clearFormData();
                }
            } catch (error) {
                console.error('提交表單時出錯:', error);
                this.$message.error('提交失敗，請稍後再試！');
            }
        },
        closeDialog() {
            this.dialogUserOperaion = false;
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
                    key: this.filterText,
                    startTime: startTime,
                    endTime: endTime,
                    ...this.userQueryDto
                };
                const response = await this.$axios.post('/user/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查詢用戶訊息異常:', error);
            }
        },
        add() {
            this.dialogUserOperaion = true;
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleFilterClear() {
            this.filterText = '';
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
            this.dialogMessageOperation = true;
            this.data = { ...row };
        },
        handleEdit(row) {
            this.dialogUserOperaion = true;
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
    /* 使按鈕水平居中 */
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 如果需要調整按鈕之間的間距 */
.customer {
    margin: 0 8px;
    /* 根據需要調整間距 */
}
</style>