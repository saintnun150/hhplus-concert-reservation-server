# Step 1: Build Stage
FROM gradle:8.10.2-jdk17 AS builder
WORKDIR /usr/src

# Gradle 캐시를 활용해 의존성 다운로드 최적화
RUN --mount=type=cache,target=/root/.gradle mkdir -p /usr/src

# 소스 코드 복사
COPY . .

# 애플리케이션 빌드 (테스트 제외)
RUN --mount=type=cache,target=/root/.gradle gradle clean build -x test

# Step 2: Runtime Stage
FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app

# JAR 파일 빌드 결과를 복사
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /usr/src/${JAR_FILE} app.jar

# 포트를 동적으로 설정할 수 있도록 ARG 및 ENV 설정
ARG PORT=8080
ENV PORT=${PORT}

# 앱 실행을 위한 포트 노출
EXPOSE ${PORT}

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]