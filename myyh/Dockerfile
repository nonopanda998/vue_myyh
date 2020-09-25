#基于centos镜像
FROM java:8
#维护人的信息
MAINTAINER The ry project <859946068@qq.com>
#开启8888端口,容器需要暴露这个地址
EXPOSE 8888
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]