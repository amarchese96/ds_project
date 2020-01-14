/usr/local/spark/bin/spark-submit \
    --master k8s://https://master.particles.dieei.unict.it:8443\
    --deploy-mode cluster \
    --name spark \
    --class org.spark.App \
    --conf spark.kubernetes.namespace=ds-project \
    --conf spark.kubernetes.authenticate.driver.serviceAccountName=deployer \
    --conf spark.executor.instances=1 \
    --conf spark.kubernetes.executor.request.cores=0.1 \
    --conf spark.kubernetes.container.image=docker-registry.default.svc:5000/ds-project/spark-prod:v1 \
    --conf spark.kubernetes.driverEnv.KAFKA_ADDRESS=kafkaa:9092 \
    --conf spark.kubernetes.driverEnv.KAFKA_MAIN_TOPIC=logs \
    --conf spark.kubernetes.driverEnv.KAFKA_GROUP_ID=spark-group \
    --conf spark.kubernetes.driverEnv.BATCH_SIZE=30 \
    --conf spark.kubernetes.driverEnv.PREVIOUS_BATCHES_NUMBER=3 \
    --conf spark.kubernetes.driverEnv.EMAIL_SENDER=marchese.angelo1@gmail.com \
    --conf spark.kubernetes.driverEnv.EMAIL_RECEIVER=marchese.angelo1@gmail.com \
    --conf spark.kubernetes.driverEnv.SENDGRID_API_KEY=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 \
    local:///opt/spark/work-dir/service.jar
