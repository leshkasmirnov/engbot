FROM java:8-jre
MAINTAINER Alexey Smirnov <aleksey.smirnov89@gmail.com>

COPY ./build/libs/engbot-1.0-SNAPSHOT.jar /app/

CMD ["mkdir", "/app/config"]

COPY ./config/logback.xml /app/config/
COPY ./config/application.yml /app/config/

WORKDIR "/app"
CMD ["java", "-Xmx256m", "-jar", "engbot-1.0-SNAPSHOT.jar"]