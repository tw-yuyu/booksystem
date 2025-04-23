<template>
    <div class="menu-container">
        <div class="menu-side" :class="{ 'menu-side-narrow': flag }">
            <div style="display: flex;align-items: center;">
                <Logo name="圖書管理" style="padding: 0 40px;margin: 15px 0;" :flag="flag" :bag="colorLogo" />
            </div>
            <div style="margin-top: 12px;">
                <AdminMenu :flag="flag" :routes="adminRoutes" :bag="bagMenu" @select="handleRouteSelect" />
            </div>
        </div>
        <div class="main">
            <div class="header-section">
                <LevelHeader @eventListener="eventListener" @selectOperation="selectOperation" :tag="tag"
                    :userInfo="userInfo" />
            </div>
            <div class="content-section">
                <router-view></router-view>
            </div>
        </div>
        <!-- 個人中心 -->
        <el-dialog :show-close="false" :visible.sync="dialogOperaion" width="26%">
            <div slot="title" style="padding: 25px 0 0 20px;">
                <span style="font-size: 18px;font-weight: 800;">個人中心</span>
            </div>
            <el-row style="padding: 10px 20px 20px 20px;">
                <el-row>
                    <p style="font-size: 12px;padding: 3px 0;margin-bottom: 10px;">
                        <span class="modelName">*頭像</span>
                    </p>
                    <el-upload class="avatar-uploader" action="/api/book-manage-sys-api/v1.0/file/upload"
                        :show-file-list="false" :on-success="handleAvatarSuccess">
                        <img v-if="userInfo.url" :src="userInfo.url" style="width: 80px;height: 80px;">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-row>
                <el-row>
                    <p style="font-size: 12px;padding: 3px 0;">
                        <span class="modelName">*用戶名</span>
                    </p>
                    <input class="input-title" v-model="userInfo.name" placeholder="用戶名">
                </el-row>
                <el-row>
                    <p style="font-size: 12px;padding: 3px 0;">
                        <span class="modelName">*用戶郵箱</span>
                    </p>
                    <input class="input-title" v-model="userInfo.email" placeholder="用戶郵箱">
                </el-row>
            </el-row>
            <span slot="footer" class="dialog-footer">
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogOperaion = false">取 消</el-button>
                <el-button size="small" style="background-color: #15559a;border: none;" class="customer" type="info"
                    @click="updateUserInfo">修改</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
import request from "@/utils/request.js";
import router from "@/router/index";
import { clearToken } from "@/utils/storage"
import AdminMenu from '@/components/VerticalMenu.vue';
import Logo from '@/components/Logo.vue';
import LevelHeader from '@/components/LevelHeader.vue';
export default {
    name: "Admin",
    components: {
        Logo,
        LevelHeader,
        AdminMenu
    },
    data() {
        return {
            adminRoutes: [],
            activeIndex: '',
            userInfo: {
                id: null,
                url: '',
                name: '',
                role: null,
                email: ''
            },
            flag: false,
            tag: '可視化',
            bag: 'rgb(250, 250, 250)',
            colorLogo: '#1c1c1c',
            bagMenu: 'rgb(250, 250, 250)',
            dialogOperaion: false
        };
    },
    created() {
        let menus = router.options.routes.filter(route => route.path == '/user')[0];//// 根據目前角色，把 /user 下的 routes 抓出來
        this.adminRoutes = menus.children;//找出 /user 頁面的所有路由設定
        this.tokenCheckLoad();
        this.menuOperationHistory();
    },

    methods: {
        async updateUserInfo() {
            try {
                const userUpdateDTO = {
                    userAvatar: this.userInfo.url,
                    userName: this.userInfo.name,
                    userEmail: this.userInfo.email
                }
                const resposne = await this.$axios.put(`/user/update`, userUpdateDTO);
                const { data } = resposne;
                if (data.code === 200) {
                    this.dialogOperaion = false;
                    this.tokenCheckLoad();
                    this.$swal.fire({
                        title: '修改個人信息',
                        text: data.msg,
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 1000,
                    });
                }
            } catch (e) {
                this.dialogOperaion = false;
                this.$swal.fire({
                    title: '修改個人信息異常',
                    text: e,
                    icon: 'error',
                    showConfirmButton: false,
                    timer: 2000,
                });
                console.error(`修改個人信息異常:${e}`);
            }
        },
        handleAvatarSuccess(res, file) {
            if (res.code !== 200) {
                this.$message.error(`頭像上傳異常`);
                return;
            }
            this.$message.success(`頭像上傳成功`);
            this.userInfo.url = res.data;
        },
        eventListener(event) {
            // 個人中心
            if (event === 'center') {
                this.dialogOperaion = true;
            }
            // 退出登錄
            if (event === 'loginOut') {
                this.loginOut();
            }
        },
        async loginOut() {
            const confirmed = await this.$swalConfirm({
                title: '退出登錄？',
                text: `推出後需重新登錄？`,
                icon: 'warning',
            });
            if (confirmed) {
                this.$swal.fire({
                    title: '退出登錄成功',
                    text: '1s 後返回登錄頁面',
                    icon: 'success',
                    showConfirmButton: false,
                    timer: 1000,
                });
                setTimeout(() => {
                    clearToken();
                    this.$router.push("/login");
                }, 1000)
            }
        },
        menuOperationHistory() {
            this.flag = sessionStorage.getItem('flag') === 'true';
        },
        selectOperation(flag) {
            this.flag = flag;
        },
        handleRouteSelect(index) {
            let ary = this.adminRoutes.filter(entity => entity.path == index);
            this.tag = ary[0].name;
            if (this.$router.currentRoute.fullPath == index) {
                return;
            }
            this.$router.push(index);
        },
        // Token檢驗
        async tokenCheckLoad() {
            try {
                const res = await request.get('user/auth');
                // 錯誤處理
                if (res.data.code === 400) {
                    this.$message.error(res.data.msg);
                    this.$router.push('/login');
                    return;
                }
                // 用戶信息賦值
                const { id, userAvatar: url, userName: name, userRole: role, userEmail: email } = res.data.data;
                this.userInfo = { id, url, name, role, email };
                // 根據角色解析路由
                const rolePath = role === 1 ? '/admin' : '/user';
                const targetMenu = router.options.routes.find(route => route.path === rolePath);
                if (targetMenu) {
                    this.routers = targetMenu.children;
                } else {
                    console.warn(`未找到與角色對應的路由：${rolePath}`);
                }
            } catch (error) {
                console.error('獲取用戶認證信息時發生錯誤:', error);
                this.$message.error('認證信息加載失敗，請重試！');
            }
        },
    }
};
</script>
<style scoped lang="scss">
.menu-container {
    display: flex;
    height: 100vh;
    width: 100%;


    .menu-side {
        width: 253px;
        min-width: 95px;
        height: 100vh;
        padding-top: 10px;
        box-sizing: border-box;
        transition: width 0.3s ease;
        background-color: rgb(250, 250, 250);
    }

    .menu-side-narrow {
        width: 115px;
    }

    .main {
        flex-grow: 1;
        overflow-x: hidden;

        .header-section {
            max-width: 100%;
            padding: 0 15px 0 0;
        }

        .content-section {
            overflow-x: hidden;
            flex-grow: 1;
            padding: 0 15px;
            box-sizing: border-box;
            overflow-y: auto;
        }
    }



}
</style>