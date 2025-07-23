# 使用 Java 17 的輕量映像
FROM eclipse-temurin:17-jdk-alpine

# 安裝 dos2unix 工具來修正 Windows 格式的換行問題
RUN apk add --no-cache dos2unix

# 設定容器內的工作目錄
WORKDIR /app

# 複製整個專案到容器內
COPY . .

# 修正 mvnw 格式並給予執行權限
RUN dos2unix mvnw && chmod +x mvnw

# 使用 Maven Wrapper 打包 Spring Boot 專案
RUN ./mvnw clean package -DskipTests

# 設定容器啟動時執行 jar 檔案
ENTRYPOINT ["java", "-jar", "target/app.jar"]

# 開放 Spring Boot 埠號（application.yml 設定）
EXPOSE 21090
# 這會在容器啟動時建立 /app/pic 並開放權限寫入圖片
# mkdir -p建立一個資料夾 /app/pic，如果中間的 /app 也不存在就一併建立
# chmod 777給這個資料夾「所有人都可讀、可寫、可執行」的權限（開放式權限）
RUN mkdir -p /app/pic && chmod 777 /app/pic