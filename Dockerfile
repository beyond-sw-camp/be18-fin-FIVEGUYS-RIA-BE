# 1. Build Stage
FROM gradle:8.6-jdk21 AS builder
WORKDIR /workspace

COPY . .
RUN gradle clean build -x test

# 2. Run Stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# JAR 파일 복사
COPY --from=builder /workspace/build/libs/*.jar app.jar

# .env.prod → 컨테이너 내부에 /app/.env 로 복사
COPY .env.prod /app/.env

# GitHub Secrets 에 저장한 Base64 → google.json 복원
ARG GOOGLE_CREDENTIALS_BASE64
RUN echo "$GOOGLE_CREDENTIALS_BASE64" | base64 -d > /app/config/google.json

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
