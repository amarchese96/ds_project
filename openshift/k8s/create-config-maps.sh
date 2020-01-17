oc create configmap video-management-service-env-file --from-env-file=./video-management-service/video-management-service-env-file.properties --save-config

oc get configmap video-management-service-env-file -o yaml > video-management-service/video-management-service-configmap.yml

oc create secret generic video-management-service-secret-file --from-env-file=./video-management-service/video-management-service-secret-file.properties --save-config

oc get secret video-management-service-secret-file -o yaml > video-management-service/video-management-service-secret-file.yml

oc create configmap video-processing-service-env-file --from-env-file=./video-processing-service/video-processing-service-env-file.properties --save-config

oc get configmap video-processing-service-env-file -o yaml > video-processing-service/video-processing-service-configmap.yml

oc create configmap spout-env-file --from-env-file=./spout/spout-env-file.properties --save-config

oc get configmap spout-env-file -o yaml > spout/spout-configmap.yml



