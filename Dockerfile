FROM node:18-alpine AS build-node

WORKDIR /workspace/frontend

COPY package.json .

RUN npm install

COPY . .

EXPOSE 5173

RUN npm run build

FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY server/mvnw .
COPY server/.mvn .mvn
COPY server/pom.xml ./
COPY server/src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT [ "java", "-cp", "app:app/lib/*", "kaffee.Application" ]