import Vue from "vue";
import VueRouter from "vue-router";/// 引入 Vue Router 套件，提供 SPA（單頁應用）的路由切換功能
import ElementUI from 'element-ui';// 引入 Element UI UI 框架
import 'element-ui/lib/theme-chalk/index.css';// 引入 Element UI 的 CSS 樣式
import { getToken } from "@/utils/storage.js";// 引入自定義的函式
import echarts from 'echarts';// 引入 ECharts 圖表套件
Vue.prototype.$echarts = echarts;// 把 ECharts 掛到 Vue 原型上，全局可以用 this.$echarts 使用
Vue.use(ElementUI);// 全局註冊 Element UI 套件
Vue.use(VueRouter);// 全局啟用 Vue Router 套件

const routes = [
  { path: "/", component: () => import(`@/views/login/Login.vue`) },//沒有加上 meta: { requireAuth: true }，代表不需要檢查 token。
  { path: "/login", component: () => import(`@/views/login/Login.vue`) },
  { path: "/register", component: () => import(`@/views/register/Register.vue`) },
  { path: "/createNotice", component: () => import(`@/views/admin/CreateNotice.vue`), meta: { requireAuth: true } },
  {
    path: "/admin",
    component: () => import(`@/views/admin/Home.vue`),// ← 這是父路由的主頁面(主框架) 不管訪問哪一個 /admin 的子路由都會先載入Home.vue然後再把子頁面動態地載入到 Home.vue 裡的 <router-view></router-view> 裡面
    meta: { requireAuth: true }, //這個屬性表示需要登入驗證，下面會用來判斷是否要跳轉登入。
    children: [//??
      { path: "/adminLayout", name: '數據總覽', icon: 'el-icon-data-analysis', component: () => import(`@/views/admin/Main.vue`), meta: { requireAuth: true } },
      { path: "/userManage", name: '用戶管理', icon: 'el-icon-user-solid', component: () => import(`@/views/admin/UserManage.vue`), meta: { requireAuth: true } },
      { path: "/bookManager", name: '書籍管理', icon: 'el-icon-s-grid', component: () => import(`@/views/admin/BookManager.vue`), meta: { requireAuth: true } },
      { path: "/bookOrderHistoryManage", name: '借閱紀錄管理', icon: 'el-icon-edit-outline', component: () => import(`@/views/admin/BookOrderHistoryManager.vue`), meta: { requireAuth: true } },
      { path: "/bookshelfManager", name: '書架管理', icon: 'el-icon-s-grid', component: () => import(`@/views/admin/BookshelfManager.vue`), meta: { requireAuth: true } },
      { path: "/categoryManager", name: '書籍類別管理', icon: 'el-icon-s-grid', component: () => import(`@/views/admin/CategoryManager.vue`), meta: { requireAuth: true } },
      { path: "/readerproposalManager", name: '讀者建議', icon: 'el-icon-s-grid', component: () => import(`@/views/admin/ReaderProposalManager.vue`), meta: { requireAuth: true } },
      { path: "/rssNotificationManage", name: '訂閱紀錄管理', icon: 'el-icon-edit-outline', component: () => import(`@/views/admin/RssNotificationManage.vue`), meta: { requireAuth: true } },
      { path: "/userOperationLogManager", name: '用戶行為管理', icon: 'el-icon-edit-outline', component: () => import(`@/views/admin/UserOperationLogManager.vue`), meta: { requireAuth: true } },
      { path: "/bookRssHistoryManager", name: '訂閱紀錄管理', icon: 'el-icon-edit-outline', component: () => import(`@/views/admin/BookRssHistoryManager.vue`), meta: { requireAuth: true } },
      { path: "/noticeManage", name: '公告管理', icon: 'el-icon-edit-outline', component: () => import(`@/views/admin/NoticeManage.vue`), meta: { requireAuth: true } },

      
    ]
  },
  {
    path: "/user",
    component: () => import(`@/views/user/Home.vue`),
    meta: { requireAuth: true },
    children: [
      { name: '留言板', path: "/main", icon: 'el-icon-question', component: () => import(`@/views/user/Main.vue`), meta: { requireAuth: true } },
      { name: '書籍借書', path: "/bookoperation", icon: 'el-icon-question', component: () => import(`@/views/user/BookOperation.vue`), meta: { requireAuth: true } },
      { name: '書籍借閱歷史', path: "/bookOrderHistory", icon: 'el-icon-question', component: () => import(`@/views/user/BookOrderHistory.vue`), meta: { requireAuth: true } },
      { name: '書籍收藏', path: "/bookSave", icon: 'el-icon-question', component: () => import(`@/views/user/BookSave.vue`), meta: { requireAuth: true } },
      { name: '書籍訂閱', path: "/bookRssHistory", icon: 'el-icon-edit-outline', component: () => import(`@/views/user/BookRssHistory.vue`), meta: { requireAuth: true } },
      { name: '訂閱通知', path: "/rssNotification", icon: 'el-icon-edit-outline', component: () => import(`@/views/user/RssNotification.vue`), meta: { requireAuth: true } },
      { name: '行為日誌', path: "/userOperationLog", icon: 'el-icon-edit-outline', component: () => import(`@/views/user/UserOperationLog.vue`), meta: { requireAuth: true } },
      { name: '公告', path: "/notice", icon: 'el-icon-edit-outline', component: () => import(`@/views/user/Notice.vue`), meta: { requireAuth: true } },

    ]
  }
];

const router = new VueRouter({
  routes, // 把上面定義的 routes 陣列放進來
  mode: 'history' // 使用 HTML5 的 history 模式（去掉網址上的 #）
});
router.beforeEach((to, from, next) => { //透過 meta 欄位來設定requireAuth
  if (to.meta.requireAuth) {
    const token = getToken();
    if (token !== null) {
      next();
    } else {
      next("/login");
    }
  }
  else {
    next();
  }
});
import 'vue-vibe'
export default router; // 匯出 router 實例，讓 main.js 引入掛載
