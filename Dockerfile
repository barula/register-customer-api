FROM openjdk:8-jre-alpine

ARG listener_port=''
ARG logs_location=''
ARG log_name=''
ARG mongo_host=''
ARG mongo_port=''

ENV listener_port_env=${listener_port}
ENV logs_location_env=${logs_location}
ENV log_name_env=${log_name}
ENV mongo_host_env=${mongo_host}
ENV mongo_port_env=${mongo_port}



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