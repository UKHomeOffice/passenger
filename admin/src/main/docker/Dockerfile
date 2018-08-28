FROM quay.io/ukhomeofficedigital/openjdk10

RUN yum install -y curl && \
    yum install -y openssl && \
    yum clean all && \
    rpm --rebuilddb

ADD admin-0.0.1-SNAPSHOT.jar /app/admin.jar
ADD scripts /app/
ADD root-certs/ /app/

ENV JAR_FILE=/app/admin.jar \
    ARGS=-Dhttp.port=8082

#Import all trusted certs

RUN chmod a+x /app/*.sh
RUN /bin/bash -c /app/import-cacerts.sh && echo "certificate import done"

EXPOSE 8082
