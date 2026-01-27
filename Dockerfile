# syntax=docker/dockerfile:1

# 빌드 스테이지: Gradle로 jar 만들기기
FROM gradle:8.7-jdk21 AS build
WORKDIR /home/gradle/project

# (캐시 효율) 먼저 빌드 설정/래퍼 복사
COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle ./gradle

# 소스 복사 
COPY src ./src

# 빌드
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /home/gradle/project/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""

ENTRYPOINT [ "sh","-c","java $JAVA_OPTS -jar /app/app.jar" ]