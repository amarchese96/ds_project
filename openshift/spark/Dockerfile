FROM maven:3-jdk-8 as builder
WORKDIR /app
COPY . .
RUN mvn package -Dmaven.test.skip=true package

FROM openjdk:8-alpine
RUN set -ex && \
    apk upgrade --no-cache && \
    ln -s /lib /lib64 && \
    apk add --no-cache bash tini libc6-compat linux-pam nss && \
    mkdir -p /opt/spark && \
    mkdir -p /opt/spark/work-dir && \
    touch /opt/spark/RELEASE && \
    rm /bin/sh && \
    ln -sv /bin/bash /bin/sh && \
    echo "auth required pam_wheel.so use_uid" >> /etc/pam.d/su && \
    chgrp root /etc/passwd && chmod ug+rw /etc/passwd

COPY ./spark-dependencies/jars /opt/spark/jars
COPY ./spark-dependencies/bin /opt/spark/bin
COPY ./spark-dependencies/sbin /opt/spark/sbin
COPY ./entrypoint.sh /opt/

ENV SPARK_HOME /opt/spark

WORKDIR /opt/spark/work-dir

COPY --from=builder /app/target/spark-1.0-SNAPSHOT-jar-with-dependencies.jar ./service.jar

ENTRYPOINT [ "/opt/entrypoint.sh" ]
