FROM twalter/openshift-nginx:mainline-alpine
COPY etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf
RUN sudo mkdir -p /storage/var 
WORKDIR /app
COPY ./src .
