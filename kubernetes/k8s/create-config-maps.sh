kubectl create configmap database-env-file --from-env-file=./video-management-service/database-env-file.properties --save-config

kubectl get configmap database-env-file -o yaml > video-management-service/database-configmap.yml

kubectl create secret generic database-secret-file --from-env-file=./video-management-service/database-secret-file.properties --save-config

kubectl get secret database-secret-file -o yaml > video-management-service/database-secret-file.yml

kubectl create configmap video-management-service-env-file --from-env-file=./video-management-service/video-management-service-env-file.properties --save-config

kubectl get configmap video-management-service-env-file -o yaml > video-management-service/video-management-service-configmap.yml

kubectl create secret generic video-management-service-secret-file --from-env-file=./video-management-service/video-management-service-secret-file.properties --save-config

kubectl get secret video-management-service-secret-file -o yaml > video-management-service/video-management-service-secret-file.yml

kubectl create configmap video-processing-service-env-file --from-env-file=./video-processing-service/video-processing-service-env-file.properties --save-config

kubectl get configmap video-processing-service-env-file -o yaml > video-processing-service/video-processing-service-configmap.yml

kubectl create configmap spout-env-file --from-env-file=./spout/spout-env-file.properties --save-config

kubectl get configmap spout-env-file -o yaml > spout/spout-configmap.yml



