# 使用 Java 17 輕量映像
FROM eclipse-temurin:17-jdk-alpine

# 設定工作目錄（容器內的根目錄）
WORKDIR /app

# 複製整個專案到容器內（含 pom.xml, src, .mvnw 等）
COPY . .

# 執行 Maven Wrapper 來打包 Spring Boot 應用
RUN ./mvnw clean package -DskipTests

# 執行打包後的 JAR 檔案
ENTRYPOINT ["java", "-jar", "target/app.jar"]  
