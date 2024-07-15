FROM node:18.16.0-alpine AS build

WORKDIR /web-ui

COPY . .

RUN npm install
RUN npm run build

FROM nginx:latest AS run

COPY --from=build /web-ui/dist /usr/share/nginx/html
COPY --from=build /web-ui/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
