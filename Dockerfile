FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

# copy files
COPY mvnw .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

# make mvnw executable
RUN chmod a+x mvnw && /src/mvnw package -Dmaven.test.skip=true
# /src/target/???

FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder /src/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

RUN apt update && apt install -y curl

ENV PORT=8080
ENV NOTICEBOARD_DB_HOST=localhost NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_USERNAME="" NOTICEBOARD_DB_PASSWORD=""
ENV PUBLISHING_SERVER_HOSTNAME=https://publishing-production-d35a.up.railway.app

EXPOSE ${PORT}

HEALTHCHECK --interval=60s --timeout=30s --start-period=120s --retries=3 \
    CMD curl ${PUBLISHING_SERVER_HOSTNAME}/health || exit 1

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar