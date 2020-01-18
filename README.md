# Distributed Systems and Big Data project - Video Server


## Preliminary Steps
### Homework 1 - Docker

- For development environment 

1. Inside the video-management-service directory, build the
 .jar file for video-management-service, launching the command:

```
	mvn package -P development -Dmaven.test.skip=true package
```

2. In the docker directory, launch the commands:
```
	sudo docker-compose build
	sudo docker-compose up
```

respectively for building and run the components.

- For production environment 

1. Use the command:
```

	sudo docker-compose up -f docker-compose.production.yml
```

### Homework 2 - Kubernetes

1. Open the terminal in the kubernetes directory, then run 
your VM with the command:
```
	minikube start --vm-driver=virtualbox --memory=5120
```

2. Build the docker component with:
```
	eval $(minikube docker-env)

	docker build -t video-management-service-prod:v1 -f video-management-service/Dockerfile-prod ./video-management-service

	docker build -t video-processing-service-prod:v1 -f video-processing-service/Dockerfile-prod ./video-processing-service

	docker build -t api-gateway-prod:v1 -f api-gateway/Dockerfile-prod ./api-gateway

	docker build -t spout-prod:v1 -f spout/Dockerfile-prod ./spout

	docker build -t spark-prod:v1 -f spark/Dockerfile-prod ./spark
```

3. Open the terminal in the k8s directory and use:
```
	sh ./create-config-maps.sh

	kubectl apply -f storage

	kubectl apply -f logs

	kubectl apply -f kafka

	kubectl apply -f video-processing-service

	kubectl apply -f video-management-service

	kubectl apply -f spout

	kubectl apply -f spark

	kubectl apply -f api-gateway

	kubectl apply -f ingress
```

