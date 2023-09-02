FROM openjdk:17-alpine

RUN apk add --update nodejs npm

WORKDIR /app

COPY ./package.json /app/

COPY ./webpack.config.js /app/

COPY ./build/dist /app/dist

COPY ./build/target /app/target

COPY ./build/ts-server /app/ts-server

RUN npm install

ENV HELLO=world
ENV NODE_ENV=prod

EXPOSE 8080
EXPOSE 3030

CMD npm start