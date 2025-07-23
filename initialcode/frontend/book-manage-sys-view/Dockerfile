# === 階段一：使用 Node.js 建構 Vue 專案 ===
FROM node:14.16.0 AS build-stage
# 使用 Node 14.16.0 映像作為第一階段的基礎（與你原本設定一致）

WORKDIR /app
# 設定工作目錄為 /app

COPY . .
# 將所有檔案複製到容器中

RUN npm install -g npm@8.19.4
# 升級 npm 到指定版本（8.19.4）

RUN npm install --legacy-peer-deps
# 安裝專案依賴，並允許舊的 peerDependencies 寫法

RUN node --max_old_space_size=2048 node_modules/.bin/vue-cli-service build
# 改用指定記憶體大小建置 Vue 專案
# 打包 Vue 專案，生成 dist 資料夾

# === 階段二：使用 Nginx 提供靜態檔案服務 ===
FROM nginx:stable-alpine AS production-stage
# 使用體積較小的 Nginx Alpine 映像作為部署階段

COPY nginx.conf /etc/nginx/conf.d/default.conf
# 複製自訂 nginx 設定

COPY --from=build-stage /app/dist /usr/share/nginx/html
# 複製打包好的 Vue 靜態檔案到 Nginx 預設目錄

EXPOSE 80
# 開放容器的 HTTP 80 port

CMD ["nginx", "-g", "daemon off;"]
# 啟動 Nginx 並常駐前景