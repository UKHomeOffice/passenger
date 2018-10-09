FROM quay.io/ukhomeofficedigital/openjdk10

RUN yum install -y curl && \
    yum install -y openssl && \
    yum clean all && \
    rpm --rebuilddb

ADD admin-0.0.1-SNAPSHOT.jar /app/admin.jar
ADD scripts /app/
ADD root-certs/ /app/

ENV USER dpadmin
ENV GROUP dpadmin
ENV JAR_FILE=/app/admin.jar \
    ARGS=-Dhttp.port=8082

RUN groupadd -r ${GROUP} && \
    useradd -r -u 1000 -g ${GROUP} ${USER}

RUN chown ${USER}:${GROUP} /app/admin.jar
RUN chown ${USER}:${GROUP} /app/*.sh

#Import all trusted certs
RUN chmod a+x /app/*.sh
RUN chmod a+x /app/admin.jar
RUN /bin/bash -c /app/import-cacerts.sh && echo "certificate import done"

EXPOSE 8082