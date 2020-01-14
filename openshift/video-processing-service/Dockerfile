FROM maven:3-jdk-8 as builder
WORKDIR /app
COPY . .
RUN mvn package -P production -Dmaven.test.skip=true package

FROM ubuntu:19.04
RUN apt-get update -y && apt-get install default-jre -y && apt-get install ffmpeg -y
WORKDIR /app
COPY --from=builder /app/target/video-processing-service-0.0.1-SNAPSHOT.jar ./service.jar
CMD java -jar service.jar
