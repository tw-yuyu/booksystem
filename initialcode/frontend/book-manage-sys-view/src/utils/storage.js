const TOKEN_KEY="token"
// const INFO = "health-info";
// const ACTIVE_PATH="active_key"
// 取得 token
export function getToken(){
    return sessionStorage.getItem(TOKEN_KEY);
}
// 設定 token
export function setToken(token){
    sessionStorage.setItem(TOKEN_KEY,token);
}
// 移除 token（不清除其他資料）
export function clearToken(){
    sessionStorage.clear();
}
// export function getActivePath(){
//     return sessionStorage.getItem(ACTIVE_PATH);
// }

// export function setActivePath(path){
//     sessionStorage.setItem(ACTIVE_PATH, path);
// }

// export function getHealthInfo(){
//     return sessionStorage.getItem(INFO);
// }

// export function setHealthInfo(obj){
//     sessionStorage.setItem(INFO,obj);
// }