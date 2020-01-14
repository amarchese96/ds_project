FROM twalter/openshift-nginx:mainline-alpine
COPY etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf
WORKDIR /app
RUN mkdir -p /storage/var
COPY ./src .
