FROM node:20-alpine AS builder

WORKDIR /app

# Install Java (required for openapi-generator)
RUN apk add --no-cache openjdk17-jre

# Copy all files needed for install and codegen
COPY package.json yarn.lock openapi.json components.json openapitools.json ./

RUN yarn install --frozen-lockfile


COPY . .
RUN yarn build

FROM nginx:alpine

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html


EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
