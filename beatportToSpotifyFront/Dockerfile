#for dev
#FROM node:14.17.4-alpine

#WORKDIR /app
#RUN npm install -g @angular/cli@12
#COPY package*.json ./
#RUN npm install
#COPY . .
#EXPOSE 4200
#CMD ["ng", "serve", "--host", "0.0.0.0"]

#for prod
FROM node:14.17.4-alpine AS build
WORKDIR /app

COPY . .

# Agrega un argumento de construcción para especificar el environment.prod.ts durante la construcción
ARG ENV_FILE=environment.prod.ts

# Copia el archivo de entorno específico durante la construcción
COPY src/environments/$ENV_FILE src/environments/environment.ts

RUN npm install
RUN npm run build
# Serve Application using Nginx Server
FROM nginx:alpine
COPY --from=build /app/dist/portfolio/ /usr/share/nginx/html
EXPOSE 80
