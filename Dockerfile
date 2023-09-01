FROM openjdk:17-alpine

RUN apk add --update nodejs npm

WORKDIR /app

COPY ./java-server /app/java-server

RUN cd /app/java-server/server && \
  apk add maven && \
  mvn clean install

COPY ./ts-server /app/ts-server

RUN cd /app/ts-server && \
  npm install && \
  npm run build

COPY ./webpack.config.js /app
COPY ./src /app/src

EXPOSE 8080
EXPOSE 3030
EXPOSE 8081

CMD cd /app/java-server/server && mvn spring-boot:run & \
    cd /app/ts-server && npm run start & \
    webpack-dev-server --config webpack.config.js