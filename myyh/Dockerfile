FROM java:8
MAINTAINER The ry project <859946068@qq.com>
EXPOSE 8888
ARG JAR_FILE
ADD ./target/${JAR_FILE} /opt/app.jar
ENTRYPOINT ["java","-jar","app.jar"]