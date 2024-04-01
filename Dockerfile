# 使用官方Java 8运行环境作为基础镜像
FROM openjdk:8-jre-slim

# 指定容器内的工作目录
WORKDIR /app

# 将构建的可执行jar文件复制到容器内的工作目录
COPY target/module_campus-0.0.1-SNAPSHOT.jar app.jar

# 声明容器应该监听的端口
EXPOSE 8080

# 指定容器启动时运行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]
