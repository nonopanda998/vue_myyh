FROM java:8
                                    ARG JAR_FILE
                                    ADD target/${JAR_FILE} app.jar
                                    EXPOSE 3005
                                    ENTRYPOINT ["java","-jar","app.jar"]