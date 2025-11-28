# 1. Build Stage
FROM gradle:8.6-jdk21 AS builder
WORKDIR /workspace

COPY . .
RUN gradle clean build -x test

# 2. Run Stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# 빌더에서 JAR 복사
COPY --from=builder /workspace/build/libs/*.jar app.jar

# .env.prod → 컨테이너 내부에 /app/.env 로 복사
COPY .env.prod /app/.env

# GitHub Secrets 의 Base64(JSON) → /app/config/google.json 복원
ARG GOOGLE_JSON_BASE64
RUN mkdir -p /app/config \
    && echo "$GOOGLE_JSON_BASE64" | base64 -d > /app/config/google.json

# Spring 에서 사용할 서비스 계정 경로 ENV
ENV GOOGLE_CALENDAR_SERVICE_ACCOUNT=/app/config/google.json

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
