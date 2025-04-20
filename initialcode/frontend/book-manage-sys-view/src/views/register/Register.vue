<template>
    <div class="login-container">
        <div class="login-panel">
            <div class="logo">
                <Logo :bag="colorLogo" name="讀者註冊" />
            </div>
            <div class="text">
                <input v-model="act" class="act" placeholder="註冊帳號" />
            </div>
            <div class="text">
                <input v-model="name" class="act" placeholder="用戶名" />
            </div>
            <div class="text">
                <input v-model="pwd" class="pwd" type="password" placeholder="輸入密碼" />
            </div>
            <div class="text">
                <input v-model="pwdConfirm" class="pwd" type="password" placeholder="確認密碼" />
            </div>
            <div>
                <span class="login-btn" @click="registerFunc">立即登錄</span>
            </div>
            <div class="tip">
                <p>已有帳號?<span class="no-act" @click="toDoLogin">返回登錄</span></p>
            </div>
        </div>
    </div>
</template>

<script>
const DELAY_TIME = 1300;
import request from "@/utils/request.js";
import md5 from 'js-md5';
import Logo from '@/components/Logo.vue';
export default {
    name: "Register",
    components: { Logo },
    data() {
        return {
            act: '', // 帳號
            pwd: '', // 密碼
            pwdConfirm: '', // 確認密碼
            name: '' // 用戶名
        }
    },
    methods: {
        // 返回登錄頁面
        toDoLogin() {
            this.$router.push('/login');
        },
        async registerFunc() {
            if (!this.act || !this.pwd || !this.pwdConfirm || !this.name) {
                this.$swal.fire({
                    title: '填寫校驗',
                    text: '帳號或密碼或用戶名不能爲空',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            if (this.pwd !== this.pwdConfirm) {
                this.$swal.fire({
                    title: '填寫校驗',
                    text: '前後密碼輸入不一致',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            const hashedPwd = md5(md5(this.pwd));
            const paramDTO = { userAccount: this.act, userPwd: hashedPwd, userName: this.name };
            try {
                const { data } = await request.post(`user/register`, paramDTO);
                if (data.code !== 200) {
                    this.$swal.fire({
                        title: '註冊失敗',
                        text: data.msg,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: DELAY_TIME,
                    });
                    return;
                }
                // 使用Swal通知註冊成功，延遲後跳轉
                this.$swal.fire({
                    title: '註冊成功',
                    text: '即將返回登錄頁...',
                    icon: 'success',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                // 根據角色延遲跳轉
                setTimeout(() => {
                    this.$router.push('/login');
                }, DELAY_TIME);
            } catch (error) {
                console.error('註冊請求錯誤:', error);
            }
        }
    }
};
</script>

<style lang="scss" scoped>
* {
    user-select: none;
}

.login-container {
    width: 100%;
    height: 100vh;
    display: flex;
    /* 啓用Flexbox佈局 */
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    flex-direction: column;
    /* 如果需要垂直居中，確保子元素也是這樣排列 */

    .login-panel {
        margin: 0 auto;
        width: 333px;
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
            font-size: 16px;
            width: 100%;
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
        border-radius: 6px;
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
            font-size: 14px;
            margin: 0;
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
                color: #568ed7;
                cursor: pointer;
            }

        }
    }

}
</style>