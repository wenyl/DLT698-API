# 使用 OpenJDK 11 作为基础镜像
FROM openjdk:11-jdk-slim

# 设置时区
ENV TZ=Asia/Shanghai

# 创建工作目录
WORKDIR /app

# 复制构建好的 JAR 包（注意文件名要与你打包后的实际文件一致）
COPY target/bs-boot-system-1.0-SNAPSHOT.jar app.jar

# 暴露端口（与你 application.yml 中的 port 保持一致）
EXPOSE 8082

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
