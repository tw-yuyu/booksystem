<template>
    <div class="login-container">
        <div class="login-panel">
            <div class="logo">
                <Logo :bag="colorLogo" name="圖書管理系統" />
            </div>
            <div class="text">
                <input v-model="act" class="act" placeholder="輸入帳號" />
            </div>
            <div class="text">
                <input v-model="pwd" class="pwd" type="password" placeholder="輸入密碼" />
            </div>
            <div>
                <span class="login-btn" @click="login">立即登入</span>
            </div>
            <div class="tip">
                <p>沒有帳號?<span class="no-act" @click="toDoRegister">點此註冊</span></p>
            </div>
        </div>
    </div>
</template>

<script>
const ADMIN_ROLE = 1;  // 管理員角色代碼
const USER_ROLE = 2;   // 一般使用者角色代碼
const DELAY_TIME = 1300; // 延遲時間（顯示 Swal 過場用）
import request from "@/utils/request.js"; //引入封裝好、客製過的 axios 實例
import { setToken } from "@/utils/storage.js"; //從 @/utils/storage.js 檔案裡引入 setToken 這個函式，這樣你這支 Vue 檔案裡就可以直接使用它！
import md5 from 'js-md5'; //md5() 來對資料進行 MD5 加密（雜湊）
import Logo from '@/components/Logo.vue';
export default {
    name: "Login",
    components: { Logo },//註冊元件才能在 template 使用
    data() {
        return {
            act: '',
            pwd: '',
            colorLogo: 'rgb(38,38,38)',
        }
    },
    created() {//當這個元件初始化時，就自動幫我執行 defaultLoad() 方法，先載入一些預設資料或設定 類似建構子
        this.defaultLoad();
    },
    methods: {
        defaultLoad() {
            const token = sessionStorage.getItem('token'); //從 sessionStorage 取出登入時儲存的 token 
            if (token === undefined || token === null || token === '') {// 如果沒有 token（使用者尚未登入），就直接結束這個方法，不做任何事。
                return;
            }
            this.$axios.get('user/auth').then(response => {//如果有 token，就呼叫後端 /user/auth API，檢查使用者身份（通常後端會根據 token 回傳使用者資料）
                const { data } = response;
                if (data.code === 400) {
                    return;
                }
                if (data.data.userRole === 1) {
                    this.$router.push('/admin'); //決定進入/admin還是/user
                } else {
                    this.$router.push('/user');
                }
            })
        },
    /**如果 有登入過（sessionStorage 有 token）：進來後會自動驗證身份 → 跳轉到 /admin 或 /user
     * 如果 沒登入過：就會卡在這個 login 畫面，等你點「立即登入」再去登入
     * 
     * 使用者進入畫面（Vue）
     ↓
        created() 自動執行 defaultLoad()
     ↓
        發送 request.get("user/auth")
     ↓
        [後端] auth() 被呼叫
     ↓
        從 LocalThreadHolder 拿目前登入者 userId
     ↓
        查資料庫得到 User 資料
     ↓
        轉成 UserVO 格式
     ↓
        回傳 Result<UserVO> 給前端
     ↓
        前端拿到 userRole 做判斷：
     -   1：導到 /admin
     - 其他：導到 /user
     */



        // 跳轉註冊頁面
        toDoRegister() {
            this.$router.push('/register');
        },
        async login() {
            if (!this.act || !this.pwd) {
                this.$swal.fire({
                    title: '填寫校驗',
                    text: '帳號或密碼不能爲空',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            const hashedPwd = md5(md5(this.pwd));
            const paramDTO = { userAccount: this.act, userPwd: hashedPwd };
            try {
                const { data } = await request.post(`user/login`, paramDTO);
                if (data.code !== 200) {
                    this.$swal.fire({
                        title: '登錄失敗',
                        text: data.msg,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: DELAY_TIME,
                    });
                    return;
                }
                setToken(data.data.token);
                // 使用Swal通知登錄成功，延遲後跳轉
                this.$swal.fire({
                    title: '登入成功',
                    text: '即將進入系統...',
                    icon: 'success',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                // 根據角色延遲跳轉
                setTimeout(() => {
                    const { role } = data.data;
                    this.navigateToRole(role);
                }, DELAY_TIME);
            } catch (error) {
                console.error('登錄請求錯誤:', error);
                this.$message.error('登錄請求出錯，請重試！');
            }
        },
        navigateToRole(role) {
            switch (role) {
                case ADMIN_ROLE:
                    this.$router.push('/admin');
                    break;
                case USER_ROLE:
                    this.$router.push('/user');
                    break;
                default:
                    console.warn('未知的角色類型:', role);
                    break;
            }
        },
    }
};
</script>

<style lang="scss" scoped>
* {
    user-select: none;
}

.login-container {
    width: 100%;
    min-height: 100vh;
    background-color: rgb(255, 255, 255);
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;

    .login-panel {
        width: 313px;
        height: auto;
        padding: 40px 30px 16px 30px;
        border-radius: 3px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.06);

        .logo {
            margin: 10px 0 30px 0;
        }

        .act,
        .pwd {
            margin: 8px 0;
            height: 53px;
            line-height: 53px;
            width: 100%;
            font-size: 16px;
            padding: 0 8px;
            box-sizing: border-box;
            border: 2px solid #c8d4e7;
            border-radius: 6px;
            padding: 0 15px;
            margin-top: 13px;
        }

        .act:focus,
        .pwd:focus {
            outline: none;
            border: 2px solid rgb(255, 126, 0);
            transition: 1.2s;
        }

        .role {
            display: inline-block;
            color: rgb(30, 102, 147);
            font-size: 14px;
            padding-right: 10px;
        }
    }

    .login-btn {
        display: inline-block;
        text-align: center;
        border-radius: 3px;
        margin-top: 20px;
        height: 43px;
        line-height: 43px;
        width: 100%;
        background-color: rgb(255, 126, 0);
        font-size: 14px !important;
        border: none;
        color: white;
        padding: 0 !important;
        cursor: pointer;
        user-select: none;
    }

    .tip {
        margin: 20px 0;

        p {
            padding: 3px 0;
            margin: 0;
            font-size: 14px;
            color: #647897;

            i {
                margin-right: 3px;
            }

            span {
                color: #3b3c3e;
                border-radius: 2px;
                margin: 0 6px;
            }

            .no-act:hover {
                color: #3e77c2;
                cursor: pointer;

            }

        }
    }

}
</style>