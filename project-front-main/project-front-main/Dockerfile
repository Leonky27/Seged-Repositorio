# ---- Build stage ----
FROM node:20 AS build
WORKDIR /app

# Install deps separately for caching
COPY package*.json ./
RUN npm ci

# Copy source
COPY . .

# Build Vite app (VITE_API_URL can be passed as build arg/ENV)
ARG VITE_API_URL
ENV VITE_API_URL=${VITE_API_URL}
RUN npm run build

# ---- Runtime stage ----
FROM nginx:alpine
# Copy build to Nginx html
COPY --from=build /app/dist /usr/share/nginx/html

# Replace Nginx default server with minimal SPA config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx","-g","daemon off;"]
