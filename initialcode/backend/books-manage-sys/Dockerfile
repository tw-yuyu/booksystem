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