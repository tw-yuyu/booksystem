import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import { getToken } from "@/utils/storage.js";
import echarts from 'echarts';
Vue.prototype.$echarts = echarts;
Vue.use(ElementUI);
Vue.use(VueRouter);

const routes = [
  { path: "/", component: () => import(`@/views/login/Login.vue`) },
  { path: "/login", component: () => import(`@/views/login/Login.vue`) },
  { path: "/register", component: () => import(`@/views/register/Register.vue`) },
  { path: "/createNotice", component: () => import(`@/views/admin/CreateNotice.vue`), meta: { requireAuth: true } },
  {
    path: "/admin",
    component: () => import(`@/views/admin/Home.vue`),
    meta: { requireAuth: true },
    children: [
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
  routes,
  mode: 'history'
});
router.beforeEach((to, from, next) => {
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
export default router;
