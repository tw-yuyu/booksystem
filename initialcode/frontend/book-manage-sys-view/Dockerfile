# === 階段一：使用 Node.js 建構 Vue 專案 ===
FROM node:14.16.0 AS build-stage  # 使用 Node 18 映像作為第一階段的基礎

WORKDIR /app                       # 設定工作目錄為 /app
COPY . .                           # 將當前專案所有檔案複製到容器中的 /app 資料夾
RUN npm install -g npm@8.19.4      # 先升級 npm 版本 # 安裝所有 npm 依賴套件
RUN npm install --legacy-peer-deps # 再安裝依賴（支援舊的 peerDeps）# 打包 Vue 專案（會產生 dist/ 資料夾）

# === 階段二：使用 Nginx 提供靜態檔案服務 ===
FROM nginx:stable-alpine AS production-stage  # 使用小型版 Nginx 映像檔

COPY nginx.conf /etc/nginx/conf.d/default.conf  # 複製自訂的 nginx 設定檔到容器內的指定位置

COPY --from=build-stage /app/dist /usr/share/nginx/html  # 將打包後的 dist 檔案複製進 nginx 預設網站資料夾

EXPOSE 80                 # 開放容器的 80 port 給外部（對應 HTTP）
CMD ["nginx", "-g", "daemon off;"]  # 啟動 nginx 並讓它常駐前景（容器不會自動結束）