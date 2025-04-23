import axios from "axios" //從 axios 這個套件引入預設匯出的東西，並命名為 axios 來使用
import { getToken } from "@/utils/storage.js"; //從 @/utils/storage.js 檔案裡引入 getToken 這個函式，這樣你這支 Vue 檔案裡就可以直接使用它！
const URL_API = process.env.VUE_APP_BASE_API;
// const URL_API = 'http://localhost:21090/api/book-manage-sys-api/v1.0'//request.get("/books") 實際會送出 http://localhost:21090/api/book-manage-sys-api/v1.0/books
console.log("API:", URL_API); //  這樣可以確保 axios 用的是哪個 API 路徑
const request = axios.create({ //加上預設設定，例如 baseURL、timeout、headers
  baseURL: URL_API,  //  API基底路徑
  timeout: 8000      //  請求逾時8秒
});
//axios 請求攔截器在每次發送 request 前，自動從 sessionStorage 取出 token，並加到 headers["token"] 裡
request.interceptors.request.use(config => {
  const token = getToken();
  if (token !== null) {
    config.headers["token"] = token; // 把 token 加到請求的 headers 裡
  }
  return config;
}, error => {
  return Promise.reject(error);
});
export default request;

// import 原始 axios → 建立自己的 axios 實例 → 加入自動帶 token 等功能 → export 出去 → 所有人都用你這版 request
