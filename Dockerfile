FROM openjdk:8-jre-alpine


ENV listener_port_env
ENV logs_location_env
ENV log_name_env
ENV mongo_host_env
ENV mongo_port_env



RUN mkdir -p /usr/src/register-customer-api/
WORKDIR /usr/src/register-customer-api/
COPY target/register-customer-api.jar .

EXPOSE 8080

ENTRYPOINT exec java \
-Dcom.huawei.port=${listener_port_env} \
-Dcom.huawei.logs.location=${logs_location_env} \
-Dcom.huawei.logs.filename=${log_name_env} \
-Dcom.huawei.datastore.host=${mongo_host_env} \
-Dcom.huawei.datastore.port=${mongo_port_env} \
-jar register-customer-api.jar
